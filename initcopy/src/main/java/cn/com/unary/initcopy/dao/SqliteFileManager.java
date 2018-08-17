package cn.com.unary.initcopy.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidDataSource;

import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.utils.AbstractLogable;
import cn.com.unary.initcopy.utils.BeanConvertUtil;
import cn.com.unary.initcopy.utils.ValidateUtils;

/**
 * 文件信息管理的基于 Sqlite 的实现
 * @author shark
 *
 */
// TODO use AOP to log
@Repository
public class SqliteFileManager extends AbstractLogable implements FileManager {

	@Autowired
	private DruidDataSource dds;
	
	@Override
	public List<FileInfo> query(String...fileIds) {
		ValidateUtils.requireNotEmpty(fileIds);
		
		StringBuilder sb = new StringBuilder("select * from FILE_INFO WHERE FILE_ID IN ('");
		for (String fileId : fileIds) {
			if (ValidateUtils.isEmpty(fileId))
				fileId = "";
			sb.append(fileId + "','");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		try (
			Connection conn = dds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(sb.toString());
		){
			return BeanConvertUtil.deSerFromResult(rset);
		} catch (SQLException e) {
			throw new InfoPersistenceException("ERROR 0x02: QUERY Error", e);
		}
	}

	@Override
	public void save(FileInfo fi) {
		Objects.requireNonNull(fi);

		try (
			Connection conn = dds.getConnection();
			Statement stmt = conn.createStatement();
		){
			stmt.execute(BeanConvertUtil.serToSql(fi));
		} catch (SQLException e) {
			throw new InfoPersistenceException("ERROR 0x02: Save Error", e);
		}
	}

	@Override
	public void save(List<FileInfo> fis) {
		for (FileInfo fi : fis) {
			this.save(fi);
		}
	}

	@Override
	public void delete(String...fileIds) {
		StringBuilder sb = new StringBuilder("DELETE FROM FILE_INFO WHERE FILE_ID IN ('");
		for (String fileId : fileIds) {
			sb.append(fileId + "',");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		try (
			Connection conn = dds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(sb.toString());
		){
			stmt.execute(sb.toString());
		} catch (SQLException e) {
			throw new InfoPersistenceException("ERROR 0x02: Batch Delete Error", e);
		} 
	}
}
