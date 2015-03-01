package net.dobachi.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import breeze.linalg.{Vector => BV, DenseVector => BDV, SparseVector => BSV}

import scala.reflect.ClassTag

/**
 * The helper class for calculation of two matrices.
 */
@SerialVersionUID(1L)
class BasicDenseMatrix(rdd: RDD[BV[Double]]) extends Serializable {
  lazy val rddWithIndex: RDD[(Long, BV[Double])] = rdd.zipWithIndex().map(p => (p._2, p._1))

  private def join(other: RDD[BV[Double]]): RDD[(BV[Double], BV[Double])] = {
    lazy val otherWithIndex: RDD[(Long, BV[Double])] = other.zipWithIndex().map(p => (p._2, p._1))
    rddWithIndex.join(otherWithIndex).map(p => (p._2._1, p._2._2))
  }

  def :+(other: RDD[BV[Double]]): RDD[BV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :+ p._2)
  }

  def :-(other: RDD[BV[Double]]): RDD[BV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :- p._2)
  }

  def :/(other: RDD[BV[Double]]): RDD[BV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :/ p._2)
  }

  def :*(other: RDD[BV[Double]]): RDD[BV[Double]] = {
    val joined = join(other)
    joined.map(p => p._1 :* p._2)
  }
}

object BasicMatrix {
  implicit def array2BDV(rdd: RDD[Array[Double]]): RDD[BDV[Double]] = {
    rdd.map(p => BDV(p))
  }
}