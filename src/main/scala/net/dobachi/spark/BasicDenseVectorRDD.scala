package net.dobachi.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import breeze.linalg.{DenseVector => BDV}

/**
 * The helper class for calculation of two RDD which have matrix-like data.
 *
 * @param rdd RDD of Breeze DenseVector which has values of Double type.
 */
@SerialVersionUID(1L)
class BasicDenseVectorRDD(rdd: RDD[BDV[Double]]) extends Serializable {

  def this(rdd: RDD[Array[Double]]) = {
    this(rdd.map(p => BDV(p)))
  }

  lazy val rddWithIndex: RDD[(Long, BDV[Double])] = rdd.zipWithIndex().map(p => (p._2, p._1))

  private def join(other: RDD[BDV[Double]]): RDD[(BDV[Double], BDV[Double])] = {
    lazy val otherWithIndex: RDD[(Long, BDV[Double])] = other.zipWithIndex().map(p => (p._2, p._1))
    rddWithIndex.join(otherWithIndex).map(p => (p._2._1, p._2._2))
  }

  def :+(other: RDD[BDV[Double]]): BasicDenseVectorRDD = {
    val joined = join(other)
    val calculatedData = joined.map(p => p._1 :+ p._2)
    new BasicDenseVectorRDD(calculatedData)
  }

  def :-(other: RDD[BDV[Double]]): BasicDenseVectorRDD = {
    val joined = join(other)
    val calculatedData = joined.map(p => p._1 :- p._2)
    new BasicDenseVectorRDD(calculatedData)
  }

  def :/(other: RDD[BDV[Double]]): BasicDenseVectorRDD = {
    val joined = join(other)
    val calculatedData = joined.map(p => p._1 :/ p._2)
    new BasicDenseVectorRDD(calculatedData)
  }

  def :*(other: RDD[BDV[Double]]): BasicDenseVectorRDD = {
    val joined = join(other)
    val calculatedData = joined.map(p => p._1 :* p._2)
    new BasicDenseVectorRDD(calculatedData)
  }

}