package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.common.utils.BeanExactUtil;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


// TODO use AOP to log
/**
 * 文件信息管理的基于 Sqlite 的实现
 * @author shark
 *
 */
public class SqliteFileManager extends AbstractLoggable implements FileManager {

	@Autowired
	private DruidDataSource dds;
    private static final String SQL = "SELECT MAX(?) FROM ?";
	@Override
	public List<FileInfoDO> queryByIds(String... fileIds) throws InfoPersistenceException {
		ValidateUtils.requireNotEmpty(fileIds);
		
		StringBuilder sb = new StringBuilder("select * from FILE_INFO WHERE FILE_ID IN ('");
		for (String fileId : fileIds) {
			if (ValidateUtils.isEmpty(fileId)) {
				fileId = "";
			}
			sb.append(fileId).append("','");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		try (
				Connection conn = dds.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL);
				ResultSet rset = stmt.executeQuery();
		){
            stmt.setInt(1, 1);
            return BeanExactUtil.deSerFromResult(rset);
		} catch (SQLException e) {
			throw new InfoPersistenceException("ERROR 0x02: QUERY Error", e);
		}
	}

	@Override
	public FileInfoDO save(FileInfoDO fi) throws InfoPersistenceException {
		Objects.requireNonNull(fi);
		try (
			Connection conn = dds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(null);
		){
			stmt.execute(BeanExactUtil.serToSql(fi));
		} catch (SQLException e) {
			throw new InfoPersistenceException("ERROR 0x02: Save Error", e);
		}
		return fi;
	}

	@Override
	public void save(List<FileInfoDO> fis) throws InfoPersistenceException {
		for (FileInfoDO fi : fis) {
			this.save(fi);
		}
	}

	@Override
	public void deleteByIds(String... fileIds) throws InfoPersistenceException {
		StringBuilder sb = new StringBuilder("DELETE FROM FILE_INFO WHERE FILE_ID IN ('");
		for (String fileId : fileIds) {
			sb.append(fileId).append("',");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		try (
			Connection conn = dds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sb.toString());
			ResultSet rset = stmt.executeQuery();
		){
			stmt.execute(sb.toString());
		} catch (SQLException e) {
			throw new InfoPersistenceException("ERROR 0x02: Batch Delete Error", e);
		} 
	}

	@Override
	public List<FileInfoDO> queryByTaskId(int taskId) {
		// TODO
		return null;
	}

	@Override
	public List<FileInfoDO> queryUnSyncFileByTaskId(int taskId) {
		return null;
	}

	@Override
	public boolean taskFinished(int taskId) {
		return false;
	}

	@Override
	public SyncTaskDO queryTask(int taskId) {
		return null;
	}

	@Override
	public void deleteTask(int taskId) {

	}

	@Override
	public SyncTaskDO saveTask(SyncTaskDO taskDO) {

		return null;
	}
}
