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
class BreezeDenseVectorRDDCalculator(rdd: RDD[BDV[Double]]) extends Serializable {
  lazy val rddWithIndex: RDD[(Long, BDV[Double])] = rdd.zipWithIndex().map(p => (p._2, p._1))

  /**
   * Helper method to get a joined RDD with other RDD[BDV]
   *
   * @param other: Other RDD
   * @return Joined data (RDD[(BDV[Double], BDV[Double]))
   */
  private def join(other: RDD[BDV[Double]]): RDD[(BDV[Double], BDV[Double])] = {
    lazy val otherWithIndex: RDD[(Long, BDV[Double])] = other.zipWithIndex().map(p => (p._2, p._1))
    rddWithIndex.join(otherWithIndex).map(p => (p._2._1, p._2._2))
  }

  def :+(other: RDD[BDV[Double]]): BreezeDenseVectorRDDCalculator = {
    val joined = join(other)
    new BreezeDenseVectorRDDCalculator(joined.map(p => p._1 :+ p._2))
  }

  def :-(other: RDD[BDV[Double]]): BreezeDenseVectorRDDCalculator = {
    val joined = join(other)
    new BreezeDenseVectorRDDCalculator(joined.map(p => p._1 :- p._2))
  }

  def :/(other: RDD[BDV[Double]]): BreezeDenseVectorRDDCalculator = {
    val joined = join(other)
    new BreezeDenseVectorRDDCalculator(joined.map(p => p._1 :/ p._2))
  }

  def :*(other: RDD[BDV[Double]]): BreezeDenseVectorRDDCalculator = {
    val joined = join(other)
    new BreezeDenseVectorRDDCalculator(joined.map(p => p._1 :* p._2))
  }
}

object BreezeDenseVectorRDDCalculator {
  def apply(rdd: RDD[BDV[Double]]) = {
    new BreezeDenseVectorRDDCalculator(rdd)
  }
}