package com.future.order.action.cook;



import java.util.List;

import javax.management.Query;

import com.future.order.base.BaseAction;
import com.future.order.entity.User;

/**
* @author by王青杰
* @version 创建时间：2017年5月29日 上午9:01:48
* 类说明
*/
public class CookdetailAction extends BaseAction{
	private User user;
	public String viewUser(){
		int id=(int) session.get("userId");
		User user=userService.viewUser(id);
		System.out.println(user);
		request.put("myself", user);
		return "viewUser";
	}
}