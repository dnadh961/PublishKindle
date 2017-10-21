package com.kindle.util.testcases;

import java.awt.AWTException;

import com.kindle.util.GenericKeywords;
import com.kindle.util.WaitHandler;

public class PublishKindle {
	static String[] authorList;
	static String[] titleList;
	static String[] descList;
	public static void main(String[] args) throws InterruptedException, AWTException {
		GenericKeywords app=new GenericKeywords();
		int noofAuthors=app.getNoofAuthors();
		authorList=app.getAuthors();
		titleList=app.getTitles();
		descList = app.getDesc();
		app.login();
		for(int i=1;i<=noofAuthors;i++){
			System.out.println("Running foruser:::  "+i);
			WaitHandler.sleep(2);
			app.click("kindle_ebook_icon_id");
			app.write("booktitle_textfield_xpath", titleList[i-1]);
			app.write("author_firstname_field_xpath",authorList[i-1].split(" ")[0]);
			app.write("author_lastname_field_xpath", authorList[i-1].split(" ")[1]);
			app.write("description_field_xpath", descList[i-1]);
			app.click("own_copyright_xpath");
			app.input("keyword1_xpath", "keyword1");
			app.input("keyword2_xpath", "keyword2");
			app.input("keyword3_xpath", "keyword3");
			app.setCatagory();
			app.click("save_and_continue_button_xpath");
			app.click("yes_radio_xpath");
			app.click("upload_ebook_button_xpath");
			System.out.println("No of windows======"+app.getTotWindowHandles());
			app.upload("book",i);
			app.click("button_warning_pdf_xpath");
			app.waitforElement("//div[@class='a-box-inner a-alert-container']//*[contains(text(),'Manuscript')]");
			app.waitforElementAbsence("//div[@class='a-alert-content' and (text()='Processing your file...')]");
			app.click("upload_JPGTIFF_only_radio_xpath");
			WaitHandler.sleep(3);
			app.click("upload_photo_button_xpath");
			app.upload("pic",i);
			app.waitforElement("//div[@id='data-assets-cover-file-upload-success']//span[contains(text(),'Cover uploaded successfully')]");
			app.click("save_and_continue_button_xpath");
			app.waitforElement("//input[@value='70_PERCENT']");
			app.click("radio_70_xpath");
			app.write("list_price_input_field_xpath", "2.99");
			WaitHandler.sleep(3);
			app.waitforElement("(//*[contains(@class, 'potter-pricing-grid-hidden-until-done')])[2]");
			app.click("save_and_publish_announce_xpath");
			app.waitforElement("//div[@class='a-popover-footer']//span[@id='publish-confirm-popover-digital-close']//input");
			app.click("test_xpath");
			app.waitforElement("//*[@id='create-digital-button']");
			WaitHandler.sleep(3);
			if(i%10==0){
				app.logout();
				app.login();
			}
		}
	}
}
