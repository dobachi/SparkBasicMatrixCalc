package net.dobachi.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import breeze.linalg.{DenseVector => BDV}

/**
 * The helper class for calculation of two matrices.
 */
@SerialVersionUID(1L)
class BasicDenseMatrix(rdd: RDD[BDV[Double]]) extends Serializable {
  lazy val rddWithIndex: RDD[(Long, BDV[Double])] = rdd.zipWithIndex().map(p => (p._2, p._1))

  private def join(other: RDD[BDV[Double]]): RDD[(BDV[Double], BDV[Double])] = {
    lazy val otherWithIndex: RDD[(Long, BDV[Double])] = other.zipWithIndex().map(p => (p._2, p._1))
    rddWithIndex.join(otherWithIndex).map(p => (p._2._1, p._2._2))
  }

  def :+(other: RDD[BDV[Double]]): RDD[BDV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :+ p._2)
  }

  def :-(other: RDD[BDV[Double]]): RDD[BDV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :- p._2)
  }

  def :/(other: RDD[BDV[Double]]): RDD[BDV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :/ p._2)
  }

  def :*(other: RDD[BDV[Double]]): RDD[BDV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :* p._2)
  }
}

object BasicMatrix {
  implicit def array2BDV(rdd: RDD[Array[Double]]): RDD[BDV[Double]] = {
    rdd.map(p => BDV(p))
  }
}