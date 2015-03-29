package net.dobachi.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import breeze.linalg.{DenseVector => BDV}

/**
 * The helper class for calculation of two RDD which have matrix-like data.
 *
 * @param rdd RDD of Breeze DenseVector which has values of Double type.
 *
 */
@SerialVersionUID(1L)
class BDVDoubleCalc(val rdd: RDD[BDV[Double]]) extends Serializable {
  lazy val rddWithIndex: RDD[(Long, BDV[Double])] = rdd.zipWithIndex().map(p => (p._2, p._1))

  /**
   * Helper method to get a joined RDD with other RDD[BDV]
   *
   * @param other: Other RDD
   * @return Joined data (RDD[(BDV[Double], BDV[Double]))
   */
  private def join(other: RDD[BDV[Double]]): RDD[(BDV[Double], BDV[Double])] = {
    val otherWithIndex: RDD[(Long, BDV[Double])] = other.zipWithIndex().map(p => (p._2, p._1))
    rddWithIndex.join(otherWithIndex).map(p => (p._2._1, p._2._2))
  }

  /**
   * Helper method to get a joined RDD with other BDVDoubleCalc's RDD[BDV]
   *
   * @param otherBDVDoubleCalc: Other BDVDoubleCalc instance
   * @return Joined data (RDD[(BDV[Double], BDV[Double]))
   */
  private def join(otherBDVDoubleCalc: BDVDoubleCalc): RDD[(BDV[Double], BDV[Double])] = {
    rddWithIndex.join(otherBDVDoubleCalc.rddWithIndex).map(p => (p._2._1, p._2._2))
  }

  def :+(other: RDD[BDV[Double]]): BDVDoubleCalc = {
    val joined = join(othzer)
    new BDVDoubleCalc(joined.map(p => p._1 :+ p._2))
  }

  def :+(otherBDVDoubleCalc: BDVDoubleCalc): BDVDoubleCalc = {
    val joined = join(otherBDVDoubleCalc)
    new BDVDoubleCalc(joined.map(p => p._1 :+ p._2))
  }

  def :-(other: RDD[BDV[Double]]): BDVDoubleCalc = {
    val joined = join(other)
    new BDVDoubleCalc(joined.map(p => p._1 :- p._2))
  }

  def :-(otherBDVDoubleCalc: BDVDoubleCalc): BDVDoubleCalc = {
    val joined = join(otherBDVDoubleCalc)
    new BDVDoubleCalc(joined.map(p => p._1 :- p._2))
  }

  def :/(other: RDD[BDV[Double]]): BDVDoubleCalc = {
    val joined = join(other)
    new BDVDoubleCalc(joined.map(p => p._1 :/ p._2))
  }

  def :/(otherBDVDoubleCalc: BDVDoubleCalc): BDVDoubleCalc = {
    val joined = join(otherBDVDoubleCalc)
    new BDVDoubleCalc(joined.map(p => p._1 :/ p._2))
  }

  def :*(other: RDD[BDV[Double]]): BDVDoubleCalc = {
    val joined = join(other)
    new BDVDoubleCalc(joined.map(p => p._1 :* p._2))
  }

  def :*(otherBDVDoubleCalc: BDVDoubleCalc): BDVDoubleCalc = {
    val joined = join(otherBDVDoubleCalc)
    new BDVDoubleCalc(joined.map(p => p._1 :* p._2))
  }
}

object BDVDoubleCalc {
  def apply(rdd: RDD[BDV[Double]]) = {
    new BDVDoubleCalc(rdd)
  }
}