/**        
 * @author: 焦祥宇 
 * @date:   createDate：2017年5月22日 上午10:47:06   
 * @Description:  
 * 
 */  
package com.future.order.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.future.order.base.BaseDao;
import com.future.order.entity.Stock;
import com.future.order.service.IStockService;
import com.future.order.util.PageCut;

@Service
public class StockDao extends BaseDao<Stock> implements IStockService {

	@Override
	public boolean AddStock(Stock stock) {
		boolean sign=false;
		try{
			sign=this.saveEntity(stock);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	@Override
	public PageCut<Stock> getPageCut(int currentPage, int pageSize) {
		String hql ;
		int count=0;
		hql = "select count(*) from Stock";
		count = ((Long) this.uniqueResult(hql)).intValue();
		PageCut<Stock> pc = new PageCut<Stock>(currentPage, pageSize, count);
		pc.setData(this.getEntityLimitList("from Stock", (currentPage-1)*pageSize, pageSize));
		return pc;
	}

	@Override
	public boolean DeletStock(int id) {
		boolean sign = false;
		try{
			String hql="delete from Stock o Where o.id='"+id+"'";
			int mark=this.executeUpdate(hql);
		if(mark==1){
			sign=true;
		}else{
			sign=false;
		}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	@Override
	public Stock CheckById(int id) {
		@SuppressWarnings("unused")
		Stock stock = new Stock();
		try{
			String hql="from Stock o where o.id='"+id+"'";
			stock=(Stock) this.uniqueResult(hql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	@Override
	public boolean UpdateStock(Stock stocks) {
		boolean sign=false;
		try{
			sign=this.updateEntity(stocks);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	@Override
	public PageCut<Stock> getSomePageCut(int currentPage, int pageSize, String inquiry) {
		String hql ;
		int count=0;
		hql = "select count(*) from Stock where site='"+inquiry+"'";
		count = ((Long) this.uniqueResult(hql)).intValue();
		PageCut<Stock> pc = new PageCut<Stock>(currentPage, pageSize, count);
		pc.setData(this.getEntityLimitList("from Stock where site='"+inquiry+"'", (currentPage-1)*pageSize, pageSize));
		return pc;
	}

	@Override
	public List<Stock> getSomestock() {
		return selectAll();
	}

	@Override
	public PageCut<Stock> getSomePageCut(int currentPage, int pageSize, Date starttime, Date endtime) {
		String hql ;
		int count=0;
		String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(starttime);
		String dateend = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(endtime);
		hql = "select count(*) from Stock where createDate between '"+dateStr+"' and '"+dateend+"'";
		count = ((Long) this.uniqueResult(hql)).intValue();
		PageCut<Stock> pc = new PageCut<Stock>(currentPage, pageSize, count);
		pc.setData(this.getEntityLimitList("from Stock where createDate between '"+dateStr+"' and '"+dateend+"'", (currentPage-1)*pageSize, pageSize));
		return pc;
	}

	@Override
	public List<Stock> getPrice(Date starttime, Date endtime) {
		String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(starttime);
		String dateend = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(endtime);
		String hql="from Stock where createDate between '"+dateStr+"' and '"+dateend+"'";
		return getEntityList(hql);
	}

	@Override
	public List<Stock> getTotal(String inquiry) {
		String hql="from Stock where site='"+inquiry+"'";
		return getEntityList(hql);
	}

}
