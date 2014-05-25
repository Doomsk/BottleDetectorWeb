package starter;

import controllers.Commander;
import global.Path;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import json.Parser;

public class WebStarter extends HttpServlet{

	private static final long serialVersionUID = 0xe096e073f4d7d334L;

	public WebStarter(){
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		doGetOrPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		doGetOrPost(req, resp);
	}

	private void doGetOrPost(HttpServletRequest req, HttpServletResponse response)
			throws IOException{

		String basePath = (new StringBuilder(String.valueOf(getServletContext().getRealPath("/")))).append("/").toString();
		Path.thisPath = basePath;
		PrintWriter out = response.getWriter();
		
		// Debugging path
		//out.println(basePath);
		
		String name = "jsonPath";
		String value = req.getParameter(name);
		String[] images = null;
		boolean imagesFromFile = false;
		
		if(value != null && value != ""){
			images = Parser.readFromLink(value);
			imagesFromFile = true;
		} else {
			name = "imagePath";
			value = req.getParameter(name);
			if(value != null && value != ""){
				images = new String[1];
				images[0] = value;
			}
		}

		if(images.length > 0){
			// By test criteria i had done a kooaba query to try to match the responses  
			//KooabaQuery kooabaQuery = new KooabaQuery();

			Commander commander = new Commander(images);			
			String responseList[][] = commander.getResults();

			/*
			for(int i = 0; i < images.length; i++){
				try{
					if(Integer.parseInt(responseList[i][0]) < 4){
						try{
							if(kooabaQuery.execute(responseList[i][2]).length() > 77)
								responseList[i][3] = "kooaba=true";
							else
								responseList[i][3] = "kooaba=false";
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				catch(Exception exception) { }
			}*/
			for(int i = 0; i < images.length; i++){
				responseList[i][3] = "void";
			}
			
			
			if(imagesFromFile){
				out.println(Parser.saveToLink(responseList, null));
			}else{
				out.println(Parser.saveAsText(responseList));
			}
			
		}else{
			out.println("failed");
		}
		
	}

}