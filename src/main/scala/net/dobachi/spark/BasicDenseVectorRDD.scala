package net.dobachi.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import breeze.linalg.{DenseVector => BDV}

/**
 * The helper class for calculation of two RDD which have matrix-like data.
 *
 * @param rdd RDD of Breeze DenseVector which has values of Double type.
 *
 * @note You can use BasicDenseVectorRDD object's implicit method
 *       to convert RDD[BDV[Double]] to RDD[Array[Double]] each other.
 */
@SerialVersionUID(1L)
class BasicDenseVectorRDD(rdd: RDD[BDV[Double]]) extends Serializable {
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

/**
 * The helper for BasicDenseVectorRDD class
 */
object BasicDenseVectorRDD {
  implicit def array2BDV(rdd: RDD[Array[Double]]): RDD[BDV[Double]] = {
    rdd.map(p => BDV(p))
  }
  
  implicit def BDV2Array(rdd: RDD[BDV[Double]]): RDD[Array[Double]] = {
    rdd.map(p => p.toArray)
  }
}