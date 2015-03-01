package net.dobachi.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._
import breeze.linalg.{Vector => BV, DenseVector => BDV, SparseVector => BSV}

import scala.reflect.ClassTag

/**
 * The helper class for calculation of two matrices.
 */
@SerialVersionUID(1L)
class BasicMatrix[T: ClassTag](rdd: RDD[BV[T]]) extends Serializable {
  lazy val rddWithIndex: RDD[(Long, BV)] = rdd.zipWithIndex().map(p => (p._2, p._1))

  private def join[T: ClassTag](other: RDD[BV[T]]): RDD[(BV[T], BV[T])] = {
    lazy val otherWithIndex: RDD[(Long, BV)] = other.zipWithIndex().map(p => (p._2, p._1))
    rddWithIndex.join(otherWithIndex).map(p => (p._2._1, p._2._2))
  }

  def :+[T: ClassTag](other: RDD[BV[T]]): RDD[BV[T]] = {
    val joined = join(other)
    joined.map(p => p._1 :+ p._2)
  }

  def :-[T: ClassTag](other: RDD[BV[T]]): RDD[BV[T]] = {
    val joined = join(other)
    joined.map(p => p._1 :- p._2)
  }

  def :/[T: ClassTag](other: RDD[BV[T]]): RDD[BV[T]] = {
    val joined = join(other)
    joined.map(p => p._1 :/ p._2)
  }

  def :*[T: ClassTag](other: RDD[BV[T]]): RDD[BV[T]] = {
    val joined = join(other)
    joined.map(p => p._1 :* p._2)
  }
}

object BasicMatrix {
  implicit def array2BDV[T: ClassTag](rdd: RDD[Array[T]]): RDD[BDV[T]] = {
    rdd.map(p => BDV(p))
  }
}