package cn.com.unary.initcopy.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants.PackType;

/**
 * 文件数据包解析器
 * @author shark
 *
 */
public interface Resolver extends AutoCloseable{
	/**
	 * 解析文件数据包，如果解析器无法对应，则会抛 IllegalStateException
	 * @param data 文件数据包
	 */
	void process (byte[] data);

    /**
     * 获取此解析器对应的打包策略
     * @return 此解析器对应的打包策略
     */
	PackType getPackType ();
}
