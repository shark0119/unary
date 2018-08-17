package cn.com.unary.initcopy.filecopy.filepacker;

import java.util.List;

import cn.com.unary.initcopy.entity.Constants.PackType;

/**
 * 全复制文件打包和解析
 * @author shark
 *
 */
public interface Packer {
	/**
	 * 开始文件读取打包，并向目标端发送数据包
	 * @param fileIds 文件的UUID
	 */
	void start (List<String> fileIds);
	/**
	 * 返回打包种类
	 * @return
	 */
	PackType getPackType ();
}
