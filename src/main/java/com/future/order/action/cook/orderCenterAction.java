package com.future.order.action.cook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.future.order.base.BaseAction;
import com.future.order.entity.Inform;
import com.future.order.entity.Ingredient;
import com.future.order.entity.Menu;
import com.future.order.entity.MenuMaterial;
import com.future.order.entity.Order;
import com.future.order.entity.OrderDetails;
import com.future.order.entity.User;
import com.future.order.util.PageCut;
import com.opensymphony.xwork2.ActionContext;
/**
 * @author by王青杰
 * @version 创建时间：2017年5月29日 下午3:38:38 类说明
 */
public class orderCenterAction extends BaseAction {
	private int OrderId;//订单ID
	private int tableId;
	private int page=1;
	private int ID;
	private int Orderid;
	private String UserName;
	private int i;
	private int menuId;
	private String input;
	ActionContext actionContext = ActionContext.getContext();
	Map session = actionContext.getSession();
	//查看所有订单
	public String checkorder() {
		PageCut<Order> pCut=orderService.getPageCut(page, 5);
		request.put("paCut",pCut);
		return "allOrder";
	}
	//查看未完成订单
	public String unfinishd() {
		PageCut<Order> pCut=orderService.getUnfinishPagCut(page, 5);
		request.put("paCut",pCut);
		return "allOrder";
	}
	//查看未完成菜品
	public String unfinishdmenu() {
		PageCut<OrderDetails> pCut=orderDetailsService.getUnfinishPageCut(page, 5);
		request.put("paCut",pCut);
		return "check";
	}
	//查看已完成菜品
	public String finish(){
		PageCut<Order> pCut=orderService.getFinishPagcut(page, 5);
		request.put("paCut",pCut);
		return "finish";
	}
	//修改订单状态为已处理(传入订单号，厨师User)
	public String DoOrder(){
		User me = (User) session.get("cook");
		ID = me.getId();
		UserName = me.getName();
		session.put("ordeID", Orderid);
		session.put("itemid", Orderid);
		boolean menu=orderService.updetemenu(Orderid,ID,UserName);
		PageCut<OrderDetails> pCut=orderDetailsService.Check(Orderid, page, 5);
		request.put("paCut",pCut);
		return "orderdetail";
	}
	//修改菜品状态为已完成，修改配料
	public String recheck() {
		User me = (User) session.get("cook"); 
		ID = me.getId();
		UserName = me.getName();
		boolean u=orderService.updetemenu(Orderid, ID, UserName);
		boolean m=orderDetailsService.updet(i,ID,UserName);
		updateIngredient(menuId);
		updateOrder(Orderid);
		PageCut<OrderDetails> pCut=orderDetailsService.Check(Orderid,page, 5);
		request.put("paCut", pCut);
		return "orderdetail";
	}
	//修改菜品状态(传入OrderId)
	public String checko() {
		User me = (User) session.get("cook");
		ID = me.getId();
		UserName = me.getName();
		session.put("itemid", Orderid);
		boolean m=orderDetailsService.updet(i,ID,UserName);
		updateIngredient(menuId);
		updateOrder(Orderid);
		PageCut<OrderDetails> pCut=orderDetailsService.getUnfinishPageCut(page, 5);
		request.put("paCut", pCut);
		return "check";
	}
	//查看餐桌详情,传入TableId
	public String check() {
		session.put("itemid", Orderid);
		PageCut<OrderDetails> pCut=orderDetailsService.Check(Orderid, page, 5);
		request.put("paCut",pCut);
		session.put("sorderid",OrderId);
		return "orderdetail";
	}
	//查看催单信息
	public String reminder() {
		PageCut<Inform> pCut=informService.getPageCut(page,5);
		request.put("paCut",pCut);
		return "inform";
	}
	public String Serach() {
		if(input==null) {
			input = (String) session.get("inp");
		}
		if(input.equals("")) {
		PageCut<Order> pCut=orderService.getPageCut(page, 5);
		request.put("paCut",pCut);
		}else {
		PageCut<Order> pCut=orderService.searchOrder(input, 5,page);
		request.put("paCut",pCut);
		}
		session.put("inp", input);
		return "allOrder";
	}
	public String SearchDetails() {
		if(input==null) {
			input = (String) session.get("inp");
		}
		if(input.equals("")) {
			PageCut<OrderDetails> pCut=orderDetailsService.getUnfinishPageCut(page, 5);
			request.put("paCut",pCut);
		}else {
		PageCut<OrderDetails> pCut=orderDetailsService.searchOrder(input, 5, page);
		request.put("paCut",pCut);
		}
		session.put("inp", input);
		return "menu";
	}
	public void updateIngredient(int menuId) {
		List  men=menuMaterialService.getMenuMaterial(menuId);
		for( int i=0;i<men.size();i++) {
			MenuMaterial pl=(MenuMaterial) men.get(i);
			int id=pl.getIngId();
			int num=(int) pl.getNum();
			boolean k=ingerdientService.updeteNum(id, num);
		}
	}
	public void updateOrder(int Orderid) {
		List <OrderDetails> list = orderDetailsService.CheckDe(Orderid);
		List n = new ArrayList();
		for(int i=0;i<list.size();i++) {
			String a=list.get(i).getStatus();
			if(a.equals("已完成")) {
				n.add(a);
			}
			if(list.size()==n.size()) {
				boolean g=orderService.updateOrder((int) session.get("itemid"));
			}
		}
	}
	
	public int getOrderId() {
		return OrderId;
	}
	public void setOrderId(int orderId) {
		OrderId = orderId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public int getOrderid() {
		return Orderid;
	}
	public void setOrderid(int orderid) {
		Orderid = orderid;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
}
