package com.drajer.eicrresponder.model;

import java.math.BigDecimal;
import org.hl7.fhir.r4.model.Timing.UnitsOfTime;
import org.hl7.fhir.r4.model.TriggerDefinition.TriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingSchedule {

  public enum TimingUnits {
    // FHIR Timing Units based on UCUM
    // s | min | h | d | wk | mo | a

    SECONDS,
    MINUTES,
    HOURS,
    DAYS,
    WEEKS,
    MONTHS,
    YEARS
  }

  private final Logger logger = LoggerFactory.getLogger(TimingSchedule.class);

  private TriggerType triggerType;

  private int numOfRepeat; // No of times to repeat
  private int maxRepeat; // Max No of repeats
  private int frequency; // No of times to repeat per period
  private int frequencyMax; // Max No of times to repeat per period
  private BigDecimal frequencyPeriod;
  private UnitsOfTime frequencyPeriodUnit;
  private BigDecimal duration;
  private UnitsOfTime durationUnit;

  public BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(BigDecimal duration) {
    this.duration = duration;
  }

  public UnitsOfTime getDurationUnit() {
    return durationUnit;
  }

  public void setDurationUnit(UnitsOfTime durationUnit) {
    this.durationUnit = durationUnit;
  }

  public int getNumOfRepeat() {
    return numOfRepeat;
  }

  public void setNumOfRepeat(int numOfRepeat) {
    this.numOfRepeat = numOfRepeat;
  }

  public int getMaxRepeat() {
    return maxRepeat;
  }

  public void setMaxRepeat(int maxRepeat) {
    this.maxRepeat = maxRepeat;
  }

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public int getFrequencyMax() {
    return frequencyMax;
  }

  public void setFrequencyMax(int frequencyMax) {
    this.frequencyMax = frequencyMax;
  }

  public BigDecimal getFrequencyPeriod() {
    return frequencyPeriod;
  }

  public void setFrequencyPeriod(BigDecimal frequencyPeriod) {
    this.frequencyPeriod = frequencyPeriod;
  }

  public UnitsOfTime getFrequencyPeriodUnit() {
    return frequencyPeriodUnit;
  }

  public void setFrequencyPeriodUnit(UnitsOfTime frequencyPeriodUnit) {
    this.frequencyPeriodUnit = frequencyPeriodUnit;
  }

  public TriggerType getTriggerType() {
    return triggerType;
  }

  public void setTriggerType(TriggerType triggerType) {
    this.triggerType = triggerType;
  }

  public void print() {

    logger.debug(
        "*** Printing Timing Schedule *** \n"
            + "Trigger Type = {}\n"
            + "Num of repeats = {}\n"
            + "Max repeats = {}\n"
            + "Frequency = {}\n"
            + "Max Frequency = {}\n"
            + "Frequency Period = {}\n"
            + "Frequency Period Unit = {}"
            + "Duration = {}\n"
            + "Duration Unit = {}",
        triggerType,
        numOfRepeat,
        maxRepeat,
        frequency,
        frequencyMax,
        frequencyPeriod,
        frequencyPeriodUnit,
        duration,
        durationUnit);
  }
}
