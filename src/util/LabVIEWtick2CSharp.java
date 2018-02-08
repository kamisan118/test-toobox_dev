package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import fsOp.FileTraversal_manager;
import fsOp.fileRead;
import fsOp.fileWrite;

public class LabVIEWtick2CSharp {

	private Date CSharpDateBase;
	private long CSharpTickBase;
	
	private Date LabVIEWDateBase;
	private long LabVIEWTickBase;
	private long LabVIEWCSharpTickBase;

	public LabVIEWtick2CSharp(){
		//Setup CSharpTimeBases
		try {
			this.CSharpDateBase = (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS")).parse("2012/03/17 03:31:10.280");
			// http://java.chinaitlab.com/base/747297.html
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.CSharpTickBase = Long.parseLong("634675518702834232");		
	}
	
	//ojbective: �q�ULabVIEW�誺��Ǧ�m
	// http://blog.csdn.net/laobai_2006/article/details/3124719
	public void SetLabVIEWTimeBasesWithCSharp(String LabVIEWStartTime, long LabVIEWstartTick)
	{
		/* 
		 * Day:  86400 sec.
		 * Hour: 3600 sec.
		 * Minute: 60 sec.
		 * Second: 1 sec.
		 */
		
//[--------不再需要了, 因為從FileRead時就已經指定好Encoding------]
//		try {
//			byte ptext[] = LabVIEWStartTime.getBytes("BIG-5");
//			LabVIEWStartTime = new String(ptext, "UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		//Setup LabVIEWTimeBases
		boolean twlhroffset = false;
		if (LabVIEWStartTime.contains("下午"))
			twlhroffset = true; 
		
		LabVIEWStartTime = LabVIEWStartTime.replaceAll(" ?上午 ?", " ");
		LabVIEWStartTime = LabVIEWStartTime.replaceAll(" ?下午  ?", " ");
		try {			
			this.LabVIEWDateBase = (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS")).parse(LabVIEWStartTime);
			
			// http://java.chinaitlab.com/base/747297.html
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.LabVIEWTickBase = LabVIEWstartTick;
		
		//(24hr��offset)
		if (twlhroffset)
		{
			this.LabVIEWDateBase.setTime(this.LabVIEWDateBase.getTime() + 43200000L);
			this.LabVIEWTickBase = LabVIEWstartTick + 43200000000L;
		}
		// -------�]��CFM_EXPR2����Ƴ��֤F12hrs(�]�������ɶ���~), �٭n�A��^��------
		this.LabVIEWDateBase.setTime(this.LabVIEWDateBase.getTime() - 43200000L);
		this.LabVIEWTickBase = LabVIEWstartTick - 43200000000L;
		
		this.LabVIEWCSharpTickBase = 621356256000034232L + this.LabVIEWDateBase.getTime() * 10000L; 
									//^^^^^^^^^^^^^^^^^ CSharp Tick, when Java tick = 0; 自己算出來的= =
		
	}
	

	
	public long LabVIEWtick2CSharp_exec(String LabVIEWtick)
	{
		long CSharpTickOffset = (Long.parseLong(LabVIEWtick) - LabVIEWTickBase) * 10L;
		
		return (this.LabVIEWCSharpTickBase + CSharpTickOffset);
	}
	
	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {
		
		/*//test
		LabVIEWtick2CSharp t1 = new LabVIEWtick2CSharp();
		t1.SetLabVIEWTimeBasesWithCSharp("2013/5/20 下午 05:43:50.686",63830686238L);
		System.out.println(String.valueOf(t1.LabVIEWtick2CSharp_exec("63830686238")));*/
		
		
		
		/*//[�ˬd�p�⵲�G]src
		Date d2 = new Date();
		d2.setTime((t1.LabVIEWtick2CSharp_exec("12757813423") - 621356256000034232L) / 10000);
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SS");
		String strDate = sdFormat.format(d2);
		System.out.println(strDate);*/
		
		
		
		
		/*
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SS");
		// http://java.chinaitlab.com/base/747297.html
		
		Date date;
		try {
			date = sdFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Date date = new Date();		
		String strDate = sdFormat.format(date);
		System.out.println(strDate);
		
			
		long d1 = Date.parse("2012-03-17,03:31:10:28");
		System.out.println(d1);
		2012/03/17 03:31:10.28
		634675518702834232*/
		
	}

}
