package com.easy.xmltest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Xml;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.text);
        //try {
        //    getXMLData();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //getSAXData();
        getPULLData();
    }

    private void getPULLData() {
        List<Book> books = null;
        Book book = null;
        try {
            //第一步，获取解析器
            XmlPullParser parser = Xml.newPullParser();
            //第二步，获取数据
            InputStream inputStream = this.getAssets().open("xmlTest.xml");
            parser.setInput(inputStream, "UTF-8");
            //第三步，获取标识类型
            int type = parser.getEventType();
            //如果等于结束标识，停止解析
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    //文档开始事件  
                    case XmlPullParser.START_DOCUMENT:
                        //初始化books列表  
                        books = new ArrayList<Book>();
                        break;
                    //解析开始事件  
                    case XmlPullParser.START_TAG:
                        //判断标签元素是否是book 
                        if ("book".equals(parser.getName())) {
                            book = new Book();
                            //得到book标签的属性值，并设置book的id
                            book.setId(parser.getAttributeValue(0));
                        }
                        if (book != null) {
                            //判断标签元素是否是name
                            if ("name".equals(parser.getName())) {
                                book.setName(parser.nextText());
                                //判断开始标签元素是否是author
                            } else if ("author".equals(parser.getName())) {
                                book.setAuthor(parser.nextText());
                            }
                        }
                        break;
                    //结束事件
                    case XmlPullParser.END_TAG:
                        if ("book".equals(parser.getName())) {
                            //将book添加到books集合
                            books.add(book);
                            book = null;
                        }
                        break;
                    default:
                }
                //下一个元素并触发相应事件
                type = parser.next();
            }//end while  
            inputStream.close();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < books.size(); i++) {
            mTextView.append(books.get(i).getId());
            mTextView.append(".");
            mTextView.append("书名：" + books.get(i).getName());
            mTextView.append("\n");
            mTextView.append("作者：" + books.get(i).getAuthor());
            mTextView.append("\n\n");
        }
    }

    public void getSAXData() {

        try {
            //第一步，创建SAX解析工厂
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            //第二步，SAX工厂生产创建SAX解析器
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //第三步，创建SAX文档处理者
            MyXMLHandler myXMLHandler = new MyXMLHandler();
            //第四步，获取xml流
            InputStream inputStream = this.getAssets().open("xmlTest.xml");
            //第五步，解析
            saxParser.parse(inputStream, myXMLHandler);
            inputStream.close();
            //第六步，获取解析完的数据
            ArrayList<Book> books = myXMLHandler.getBooks();
            for (int i = 0; i < books.size(); i++) {
                mTextView.append(books.get(i).getId());
                mTextView.append(".");
                mTextView.append("书名：" + books.get(i).getName());
                mTextView.append("\n");
                mTextView.append("作者：" + books.get(i).getAuthor());
                mTextView.append("\n\n");
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getXMLData() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = this.getAssets().open("xmlTest.xml");
            //第一步，创建DocumentBuilderFactory工厂对象
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //第二步，通过工厂对象创建DocumentBuilder建造者
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //第三步，创建Document存放整个xml的数据
            Document document = documentBuilder.parse(inputStream);
            //第四步，获取xml根节点
            Element element = document.getDocumentElement();
            //第五步，使用根节点和子节点名称获取所有子节点为‘book’的节点
            NodeList bookList = element.getElementsByTagName("book");


            //第一种方式通过Element和具体标签名获取值
            for (int i = 0; i < bookList.getLength(); i++) {
                //获取某个book标签元素
                Element book = (Element) bookList.item(i);
                //获取标签的id属性，并显示到TextView上
                mTextView.append(book.getAttribute("id"));
                //更具标签名获取name标签信息
                NodeList bookName = book.getElementsByTagName("name");
                //因为name没有子节点了所以省略了遍历直接取了第一个值，拿到name节点
                Node item = bookName.item(0);
                //获取name值并显示到TextView上
                mTextView.append("书名：" + item.getTextContent() + "\n");

                NodeList author = book.getElementsByTagName("author");
                Node authorItem = author.item(0);
                mTextView.append("作者：" + authorItem.getTextContent() + "\n\n");
            }
            //添加分隔线
            mTextView.append("\n --------------------------------------------------\n\n");

            //第二种方式通过Node节点获取值
            for (int i = 0; i < bookList.getLength(); i++) {
                //获取某个book标签节点
                Node book = bookList.item(i);
                //获取标签的属性列表，并显示到TextView上
                NamedNodeMap attributes = book.getAttributes();
                Node id = attributes.getNamedItem("id");
                mTextView.append(id.getTextContent());
                //获取book节点的子节点
                NodeList bookInfos = book.getChildNodes();
                for (int j = 0; j < bookInfos.getLength(); j++) {
                    //获取某个子节点
                    Node bookInfo = bookInfos.item(j);
                    //判断节点名
                    if ("name".equals(bookInfo.getNodeName())) {
                        //获取相应节点数据
                        String name = bookInfo.getTextContent();
                        //获取name标签值
                        mTextView.append("书名：" + name + "\n");
                    } else if ("author".equals(bookInfo.getNodeName())) {
                        String author = bookInfo.getTextContent();
                        mTextView.append("作者：" + author + "\n\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }
}
