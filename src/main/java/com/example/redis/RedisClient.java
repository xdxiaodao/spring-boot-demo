package com.example.redis;

/**
 * @author zhangmuzhao
 * @email zhangmuzhao@sogou-inc.com
 * @copyright (C) http://git.sogou-inc.com/adstream
 * @date 2017/10/13 19:23
 * @desc
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
@Component
public class RedisClient {
  private static final int DEFAULT_DB = 0;
  private static final String STRING_ERROR = "0";
  private static final String STRING_SUCCESS = "OK";
  @Autowired(required = false)
  protected JedisPool jedisPool;
  protected Jedis jedis;
  private static ThreadLocal<Jedis> jedisLocal = new ThreadLocal<Jedis>();
  public void setJedisPool(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }
  public void setJedis(Jedis jedis) {
    this.jedis = jedis;
  }
  public String set(String key, String value) {
    try {
      return getJedis().set(key, value);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String set(byte[] key, byte[] value) {
    try {
      return getJedis().set(key, value);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String get(String key) {
    try {
      return getJedis().get(key);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public byte[] get(byte[] key) {
    try {
      return getJedis().get(key);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public <T> T getObject(String key) {
    try {
      return (T) unserialize(getJedis().get(key.getBytes()));
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public <T> String setObject(String key, T object) {
    try {
      return getJedis().set(key.getBytes(), serialize(object));
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Long del(String key) {
    try {
      return getJedis().del(key);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String msetBitSet(List<String> keys, List<BitSet> values) {
    if (CollectionUtils.isEmpty(keys) && CollectionUtils.isEmpty(values)) {
      return STRING_SUCCESS;
    }
    Assert.isTrue(CollectionUtils.isNotEmpty(keys) && CollectionUtils.isNotEmpty(values)
        && keys.size() == values.size(), "please check your input data");
    byte[][] keysvalues = new byte[keys.size() + values.size()][];
    for (int index = 0; index < keys.size(); index++) {
      keysvalues[2 * index] = keys.get(index).getBytes();
      keysvalues[2 * index + 1] = bitSet2ByteArray(values.get(index));
    }
    try {
      return mset(keysvalues);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String msetBitSet(Map<String, BitSet> key2Value) {
    if (MapUtils.isEmpty(key2Value)) {
      return STRING_SUCCESS;
    }
    List<String> keys = Lists.newArrayList();
    List<BitSet> values = Lists.newArrayList();
    for (Map.Entry<String, BitSet> entry : key2Value.entrySet()) {
      keys.add(entry.getKey());
      values.add(entry.getValue());
    }
    try {
      return msetBitSet(keys, values);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public List<BitSet> mgetBitSet(String... keys) {
    byte[][] bkeys = new byte[keys.length][];
    for (int index = 0; index < keys.length; index++) {
      bkeys[index] = keys[index].getBytes();
    }
    List<byte[]> values = null;
    try {
      values = mget(bkeys);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
    if (getFirstNotNull(values) == null) {
      return Collections.EMPTY_LIST;
    }
    return Lists.transform(values, new Function<byte[], BitSet>() {
      @Override
      public BitSet apply(byte[] input) {
        return byteArray2BitSet(input);
      }
    });
  }
  public Set<String> keys(String pattern) {
    try {
      return getJedis().keys(pattern);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public <T> Set<T> keysObject(String pattern) {
    Set<T> result = Sets.newHashSet();
    Set<byte[]> bResult = null;
    try {
      bResult = getJedis().keys(pattern.getBytes());
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
    for (byte[] bytes : bResult) {
      result.add((T) unserialize(bytes));
    }
    return result;
  }
  public Boolean exists(String key) {
    try {
      return getJedis().exists(key);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Boolean setbit(String key, long offset, boolean value) {
    try {
      return getJedis().setbit(key, offset, value);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Boolean getbit(String key, long offset) {
    try {
      return getJedis().getbit(key, offset);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String setBitSet(String key, BitSet bitSet) {
    return set(key.getBytes(), bitSet2ByteArray(bitSet));
  }
  public BitSet getBitSet(String key) {
    byte[] value = get(key.getBytes());
    return value == null ? new BitSet() : byteArray2BitSet(value);
  }
  public String mset(String... keysvalues) {
    if (ArrayUtils.isEmpty(keysvalues)) {
      return STRING_SUCCESS;
    }
    try {
      return getJedis().mset(keysvalues);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String mset(byte[]... keysvalues) {
    if (ArrayUtils.isEmpty(keysvalues)) {
      return STRING_SUCCESS;
    }
    try {
      return getJedis().mset(keysvalues);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public List<String> mget(String... keys) {
    if (ArrayUtils.isEmpty(keys)) {
      return Collections.EMPTY_LIST;
    }
    try {
      return getJedis().mget(keys);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public List<byte[]> mget(byte[]... keys) {
    if (ArrayUtils.isEmpty(keys)) {
      return Collections.EMPTY_LIST;
    }
    try {
      return getJedis().mget(keys);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Long lpush(String key, String... values) {
    try {
      return getJedis().lpush(key, values);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String lindex(String key, long index) {
    try {
      return getJedis().lindex(key, index);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String lset(String key, long index, String value) {
    try {
      return getJedis().lset(key, index, value);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String hmset(String key, Map<String, String> hash) {
    try {
      return getJedis().hmset(key, hash);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public String hget(String key, String filedName) {
    try {
      return getJedis().hget(key, filedName);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public List<String> hmget(String key, String... fields) {
    try {
      return getJedis().hmget(key, fields);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Map<String, String> hgetAll(String key) {
    try {
      return getJedis().hgetAll(key);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Long bitop(BitOP op, String destKey, String... srcKeys) {
    try {
      return getJedis().bitop(op, destKey, srcKeys);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
    try {
      return getJedis().bitop(op, destKey, srcKeys);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  public Set<String> smembers(String key) {
    try {
      return getJedis().smembers(key);
    } catch (Exception e) {
      releaseJedis();
      throw e;
    }
  }
  // @Override
  // public String flushAll() {
  // Jedis jedis = getJedis();
  // try {
  // return jedis.flushAll();
  // } finally {
  // close(jedis);
  // }
  // }
  //
  // @Override
  // public String flushDB() {
  // Jedis jedis = getJedis();
  // try {
  // return jedis.flushDB();
  // } finally {
  // close(jedis);
  // }
  // }
  public Jedis getJedis() {
    Jedis jedis = jedisLocal.get();
    if (null != jedis) {
      try {
        // 取出来后执行ping检查下是否依然存活
        if (jedis.isConnected()) {
          return jedis;
        }
      } catch (Exception e) {
        jedis.close();
      }
    }
    jedis = jedisPool.getResource();
    jedisLocal.set(jedis);
    return jedis;
  }
  public Jedis getJedis(int db) {
    Jedis jedis = jedisPool.getResource();
    jedis.select(db);
    return jedis;
  }
  /**
   * 释放
   */
  public void releaseJedis() {
    Jedis jedis = jedisLocal.get();
    if (null != jedis) {
      try {
        jedis.close();
      } catch (Exception e) {
      }
    }
    jedisLocal.remove();
  }
  //
  // private void close(Jedis jedis) {
  // jedis.close();
  // }
  private byte[] serialize(Object object) {
    if (object == null) {
      return null;
    }
    ObjectOutputStream oos;
    ByteArrayOutputStream baos;
    try {
      // 序列化
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      byte[] bytes = baos.toByteArray();
      return bytes;
    } catch (Exception e) {
      Throwables.propagate(e);
    }
    return null;
  }
  private Object unserialize(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    ByteArrayInputStream bais;
    try {
      // 反序列化
      bais = new ByteArrayInputStream(bytes);
      ObjectInputStream ois = new ObjectInputStream(bais);
      return ois.readObject();
    } catch (Exception e) {
      Throwables.propagate(e);
    }
    return null;
  }
  /**
   * 将BitSet对象转化为ByteArray
   *
   * @param bitSet
   * @return
   */
  private byte[] bitSet2ByteArray(BitSet bitSet) {
    byte[] bytes = new byte[bitSet.size() / 8];
    for (int i = 0; i < bitSet.size(); i++) {
      int index = i / 8;
      int offset = 7 - i % 8;
      bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
    }
    return bytes;
  }
  /**
   *
   * @param bytes
   * @return
   */
  private BitSet byteArray2BitSet(byte[] bytes) {
    BitSet bitSet = new BitSet(bytes.length * 8);
    int index = 0;
    for (int i = 0; i < bytes.length; i++) {
      for (int j = 7; j >= 0; j--) {
        bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true : false);
      }
    }
    return bitSet;
  }
  /**
   * 多个BitSet的or操作
   *
   * @param bitSets
   * @return
   */
  private BitSet multiOr(Collection<BitSet> bitSets) {
    BitSet bitSet = new BitSet();
    for (BitSet eachBitSet : bitSets) {
      bitSet.or(eachBitSet);
    }
    return bitSet;
  }
  /**
   * 多个BitSet的and操作
   *
   * @param bitSets
   * @return
   */
  private BitSet multiAnd(Collection<BitSet> bitSets) {
    if (bitSets.isEmpty()) {
      return new BitSet();
    }
    BitSet bitSet = null;
    for (BitSet eachBitSet : bitSets) {
      if (null == bitSet) {
        bitSet = eachBitSet;
      } else {
        bitSet.and(eachBitSet);
      }
    }
    return bitSet;
  }
  /**
   * 将第一个BitSet上对应第二个BitSet上所有为1的位置的值全部置为value,生成一个新的BitSet
   *
   * @param f
   * @param s
   * @param value
   */
  private BitSet setbit(BitSet f, BitSet s, boolean value) {
    BitSet result = new BitSet();
    result.or(f);
    if (value) {
      result.or(s);
    } else {
      result.andNot(s);
    }
    return result;
  }
  /**
   * 寻找集合中第一个不是null的元素
   *
   * @param dataList
   * @param <T>
   * @return
   */
  private <T> T getFirstNotNull(Collection<T> dataList) {
    if (CollectionUtils.isEmpty(dataList)) {
      return null;
    }
    for (T t : dataList) {
      if (t != null) {
        return t;
      }
    }
    return null;
  }
}
