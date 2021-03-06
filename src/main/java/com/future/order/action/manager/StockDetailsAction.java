package com.future.order.action.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.future.order.base.BaseAction;
import com.future.order.entity.Ingredient;
import com.future.order.entity.StockDetails;
import com.future.order.util.PageCut;

public class StockDetailsAction extends BaseAction {
	private static final long serialVersionUID = 5615491407103245053L;
	private int id;
	private int stocksid = 0;//进货id
	private int page = 1;
	private StockDetails details;
	private String ask;//所查询属性
	private String inquiry;//查询属性值

	private File myFileName;//富文本编辑器上传图片对应的统一文件名称

	/**
	 * @return the myFileName
	 */
	public File getMyFileName() {
		return myFileName;
	}

	/**
	 * @param myFileName
	 *            the myFileName to set
	 */
	public void setMyFileName(File myFileName) {
		this.myFileName = myFileName;
	}

	public String CheckStockDetails() {//把进货id放入session中，方便下面方法取用
		session.put("stockid", id);
		return this.execute();
	}

	public String execute() {// 根据订单ID查询订单详细信息并分页
		int stockid = (int) session.get("stockid");
		PageCut<StockDetails> pCut = stockDetailsService.getPageCut(page, 8, stockid);
		if (pCut.getData().size() == 0) {
			String mark = "进货详细信息为空";
			request.put("markinfo", mark);
		}
		request.put("adss", "execute");
		request.put("pc", pCut);
		return "details";
	}

	public String AddDetails() {//添加进货详细信息
		String ingName = details.getIngName();
		Date createDate = (Date) session.get("createDate");
		int stockId = (int) session.get("stockId");
		String place = (String) session.get("site");
		details.setCreateDate(createDate);
		details.setStockId(stockId);
		details.setPlace(place);
		@SuppressWarnings("unchecked")
		List<Ingredient> list = (List<Ingredient>) session.get("Ientlist");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(ingName)) {
				details.setIngId(list.get(i).getId());
			}
		}
		//得到配料的所有信息
		List<Ingredient> alllist = ingerdientService.getNews();
		boolean sign = false;
		for (int i = 0; i < alllist.size(); i++) {
			if (details.getIngName().equals(alllist.get(i).getName())) {
				alllist.get(i).setNum(alllist.get(i).getNum() + details.getNum());
				alllist.get(i).setPrice(details.getPrice());
				//修改配料的价格和库存
				sign = ingerdientService.updateIngredient(alllist.get(i));
			}
		}
		if (details.getOrigins().equals("<p><br></p>")) {
			details.setOrigins("<p>暂无溯源信息</p>");
		}
		boolean boo = stockDetailsService.addDetails(details);
		if (boo && sign) {
			request.put("addMsg", "添加成功");
		} else {
			request.put("addMsg", "添加失败！");
		}
		return "add";

	}

	public String delet() {// 根据订单详细信息的ID删除所有该订单的详细信息的一条信息
		boolean sign = stockDetailsService.deletDetails(stocksid);
		String mark = "操作失败";
		if (sign == true) {
			mark = "删除成功";
		} else {
			mark = "删除失败";
		}
		request.put("deleteMark", mark);
		return this.execute();
	}

	public String toUpdate() {// 根据ID获得需要修改的订单信息
		//获取进货的详细信息中的一条，用于预览溯源信息
		StockDetails stockDetails = stockDetailsService.checkById(stocksid);
		String place = stockDetails.getPlace();
		double Num = stockDetails.getNum();
		session.put("place", place);
		session.put("Num", Num);
		stockDetails.setOrigins(stockDetails.getOrigins().replace("'", "\""));
		request.put("stockDetails", stockDetails);
		return "update";
	}

	public String preview() {//获取进货的详细信息中的一条，用于预览溯源信息
		StockDetails stockDetails = stockDetailsService.checkById(stocksid);
		stockDetails.setOrigins(stockDetails.getOrigins().replace("'", "\""));
		request.put("stockDetails", stockDetails);
		return "preview";
	}

	public String Update() {// 接收修改后的订单信息用于修改
		//得到所有配料，进货详情使用
		List<Ingredient> alllist = ingerdientService.getNews();
		boolean sign = false;
		double num = (double) session.get("Num");
		for (int i = 0; i < alllist.size(); i++) {
			if (details.getIngName().equals(alllist.get(i).getName())) {
				alllist.get(i).setNum(alllist.get(i).getNum() - num + details.getNum());
				alllist.get(i).setPrice(details.getPrice());
				//修改配料的价格和库存
				sign = ingerdientService.updateIngredient(alllist.get(i));
			}
		}
		String place= (String) session.get("place");
		details.setPlace(place);
		if (details.getOrigins().equals("<p><br></p>")) {
			details.setOrigins("<p>暂无溯源信息</p>");
		}
		boolean boo = stockDetailsService.updatestocks(details);
		String mark = "操作失败";
		if (sign && boo) {
			mark = "修改成功";
		} else {
			mark = "修改失败";
		}
		request.put("markinfo", mark);
		return this.execute();
	}

	public String Inquiry() {//用于条件查询，根据条件查询进货详情的信息
		int stockid = (int) session.get("stockid");
		PageCut<StockDetails> pCut = new PageCut<StockDetails>();
		if (inquiry == null) {
			inquiry = (String) session.get("inquiry");
		}
		pCut = stockDetailsService.getSomePageCut(page, 8, stockid, inquiry);
		// 获得全部订单信息
		if (pCut.getData().size() == 0) {
			String mark = "进货详细信息为空";
			request.put("markinfo", mark);
		}
		request.put("pc", pCut);
		request.put("adss", "Inquiry");
		session.put("ask", ask);
		session.put("inquiry", inquiry);
		return "details";
	}
   //用于富文本编辑器的图片上传
	public void uploadImg() throws Exception {
		HttpServletRequest req2 = ServletActionContext.getRequest();
		HttpServletResponse res2 = ServletActionContext.getResponse();
		String name = myFileName.getName();
		// 提取文件拓展名
		String fileNameExtension = name.substring(name.indexOf("."), name.length() - 1);
		// 生成实际存储的真实文件名
		String realName = UUID.randomUUID().toString() + fileNameExtension;
		// 图片存放的真实路径
		String realPath = req2.getServletContext().getRealPath("/uploadImg/source") + "/" + realName;
		// 将文件写入指定路径下
		InputStream in = new FileInputStream(myFileName);
		File uploadFile = new File(realPath);
		OutputStream out = new FileOutputStream(uploadFile);
		byte[] buffer = new byte[1024 * 1024];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.close();

		// 返回图片的URL地址
		res2.getWriter().write(req2.getContextPath() + "/uploadImg/source/" + realName);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStocksid() {
		return stocksid;
	}

	public void setStocksid(int stocksid) {
		this.stocksid = stocksid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public StockDetails getDetails() {
		return details;
	}

	public void setDetails(StockDetails details) {
		this.details = details;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getInquiry() {
		return inquiry;
	}

	public void setInquiry(String inquiry) {
		this.inquiry = inquiry;
	}

}
