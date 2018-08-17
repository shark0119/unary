package cn.com.unary.initcopy.mock;
/**
 * 传输模块服务端接口
 * @author shark
 *
 */
public interface UnaryTransferServer {

	/**
	 * 设置数据处理类
	 * @param process
	 */
	void setProcess (UnaryProcess process);
	
	/**
	 * 设置监听端口
	 * @param port
	 */
	void start (int port);
}
