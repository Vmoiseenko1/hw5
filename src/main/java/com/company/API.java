package com.company;

import kong.unirest.Unirest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


//Для получаемых данных по API требуется создать соответствующую объектную модель
//        (класс для сохранения этих данных)
public class API {
    public static String id = "";
    public static String name = "";
    public static String val = "";
    public static void getResponse(String date, String currency) {
        try {
            String response = Unirest.get("https://www.cbr.ru/scripts/XML_daily.asp?date_req={date}").routeParam("date", date).asString().getBody(); // получаем xml с любым запросом по дате

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); // подготовка к получению билдера документов
            DocumentBuilder docBuilder;
            docBuilder = builderFactory.newDocumentBuilder(); // билдер, который парсит XML, создает структуру Document в виде иерархического дерева
            Document document = docBuilder.parse(new InputSource(new StringReader(response))); // сам документ

            String errorText = document.getDocumentElement().getTextContent();
            //System.out.print(errorText);
            if (errorText.replace("\n", "").equals("Error in parameters")) {
                throw new WrongData();
            }

            NodeList charCodes = document.getDocumentElement().getElementsByTagName("CharCode");
            for (int i = 0; i < charCodes.getLength(); i++) {
                Node charCode = charCodes.item(i);
                String docCurrency = charCode.getTextContent();
                System.out.println(docCurrency + "HH");
                if (docCurrency.equals(currency)) {
                    //System.out.println(charCode.getParentNode().getAttributes().getNamedItem("ID") + "AA");
                    id = charCode.getParentNode().getAttributes().getNamedItem("ID").toString();
                    System.out.println(id);
                    break;
                }
            }
            if (id.equals("")) throw new WrongCurrency();
            NodeList valutes = document.getDocumentElement().getElementsByTagName("Valute");
            for (int i = 0; i < valutes.getLength(); i++) {
                Node valute = valutes.item(i);
                String docValute = valute.getAttributes().getNamedItem("ID").toString();
                if (docValute.equals(id)) {
//                    System.out.println("Совпало");
//                    System.out.println(docValute);
                    NodeList list = valute.getChildNodes();
//                    System.out.println(list);
                    for (int iter = 0; iter < list.getLength(); iter++) {
                        Node parametr = list.item(iter);
                        if (parametr.getNodeName().equals("Name")) {
                            name = parametr.getTextContent();
                        }
                        if (parametr.getNodeName().equals("Value")) {
                            val = parametr.getTextContent();
                        }
                    }
                    break;
                }
            }
            System.out.println("1 " + name + " = " + val + " Российских рубля");
        } catch (WrongData | WrongCurrency e) {
            System.out.println(e);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
