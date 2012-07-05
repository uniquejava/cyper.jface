package hello;

import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.kitten.core.C;
import org.kitten.core.util.JDomUtil;
import org.kitten.core.util.MD5;

public class HelloXml {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws Exception {
		String filepath =  C.UD+"/connections.xml";
		Document doc = JDomUtil.buildDocumentFromFile(filepath);
		Element root = doc.getRootElement();
		String name = "localhost_sample1";
		Element node = (Element) XPath.selectSingleNode(doc, "/connections/connection[@name='"+name+"']");
		if (node!=null) {
			System.out.println("exist!");
			return;
		}else{
			System.out.println("not");
		}
		
		
		Element connection = new Element("connection");
		connection.setAttribute("name",name);
		Element host = new Element("host");
		host.setText("localhost");
		connection.addContent(host);
		
		Element port = new Element("port");
		port.setText("50000");
		connection.addContent(port);
		
		Element db = new Element("database");
		db.setText("SAMPLE");
		connection.addContent(db);
		
		Element username = new Element("username");
		username.setText("CYPER.YIN");
		connection.addContent(username);
		
		Element password = new Element("password");
		password.setText(MD5.encrypt("111111"));
		connection.addContent(password);
		
		root.addContent(connection);
		
		try {
			JDomUtil.save(doc,filepath,"UTF-8",JDomUtil.FORMAT_PRETTY);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
