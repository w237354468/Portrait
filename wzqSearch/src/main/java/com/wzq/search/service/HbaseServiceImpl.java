package com.wzq.search.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HbaseServiceImpl {
    private static Admin admin = null;
    private static Connection conn = null;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir", "hdfs://:9000/hbase");
        conf.set("hbase.zookeeper.quorum", ".168");
        conf.set("hbase.client.scanner.timeout.period", "60000");
        conf.set("hbase.rpc.timeout", "60000");
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(String tableName, String rowKey, String familyName, String column)
            throws Exception {

        Table table = conn.getTable(TableName.valueOf(tableName));
        byte[] rowkeyByte = Bytes.toBytes(rowKey);
        Get get = new Get(rowkeyByte);
        Result result = table.get(get);
        byte[] resultByte = result.getValue(familyName.getBytes(), column.getBytes());
        if (resultByte == null) {
            return null;
        }

        return new String(resultByte);
    }
}
