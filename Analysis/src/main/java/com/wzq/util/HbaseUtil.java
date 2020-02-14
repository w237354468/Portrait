package com.wzq.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseUtil {

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

  public void createTable(String tableName, String familyName) throws IOException {
    HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
    HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(familyName);
    hTableDescriptor.addFamily(hColumnDescriptor);

    admin.createTable(hTableDescriptor);
    System.out.println("over");
  }

  public static void putData(
      String tableName, String rowKey, String familyName, Map<String, String> data)
      throws IOException {
    Table table = conn.getTable(TableName.valueOf(tableName));
    byte[] rowkeyByte = Bytes.toBytes(rowKey);
    Put newPut = new Put(rowkeyByte);
    if (data != null) {
      Set<Entry<String, String>> set = data.entrySet();
      for (Entry<String, String> entry : set) {
        String key = entry.getKey();
        String value = entry.getValue();
        newPut.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(key), Bytes.toBytes(value));
      }
    }
    table.put(newPut);
    table.close();
    System.out.println("put ok");
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

  public static void putData(String tableName, String rowKey, String familyName, String column,String data)
      throws IOException {
    Table table = conn.getTable(TableName.valueOf(tableName));
    Put newPut = new Put(rowKey.getBytes());
    newPut.addColumn(familyName.getBytes(), column.getBytes(), data.getBytes());
    table.put(newPut);
  }

  public static void main(String[] args) throws Exception {
    System.setProperty("hadoop.home.dir", "E:/");
    String data = getData("baseuserscaninfo", "1", "time", "firstvisittime");
    System.out.println(data);
  }
}
