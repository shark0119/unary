package cn.com.unary.initcopy.filecopy;

import api.UnaryTServer;
import cn.com.unary.initcopy.filecopy.fileresolver.Resolver;
import cn.com.unary.initcopy.utils.AbstractLogable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("serverFileCopy")
public class ServerFileCopy extends AbstractLogable {

    @Autowired
    protected Resolver syncAllResolver;
    @Autowired
    protected Resolver rsyncResolver;
    @Autowired
    protected UnaryTServer unaryTServer;


}
