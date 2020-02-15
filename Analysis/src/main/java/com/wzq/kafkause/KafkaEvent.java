package com.wzq.kafkause;

public class KafkaEvent {

  private String word;
  private int frequency;
  private long timestamp;

  public KafkaEvent() {}

  public KafkaEvent(String word, int frequency, long timestamp) {
    this.setWord(word);
    this.setFrequency(frequency);
    this.setTimestamp(timestamp);
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public static KafkaEvent fromString(String eventString) {
    String[] split = eventString.split("##1##");
    return new KafkaEvent(split[0], Integer.parseInt(split[1]), Long.parseLong(split[2]));
  }

  @Override
  public String toString() {
    return "KafkaEvent{"
        + "word='"
        + word
        + '\''
        + ", frequency="
        + frequency
        + ", timestamp="
        + timestamp
        + '}';
  }
}
