package org.cosysoft.device.node.domain.test;

import org.cosysoft.device.node.domain.Result;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 兰天 on 2015/4/9.
 */
public class ResultTest {

    @Test
    public void testArrayResult() {
        Result<List<String>> at = new Result();
        List<String> a = new ArrayList<>();
        a.add("p1");
        at.setPayload(a);
        Assert.assertTrue(at.getPayload().get(0) instanceof String);
    }
}
