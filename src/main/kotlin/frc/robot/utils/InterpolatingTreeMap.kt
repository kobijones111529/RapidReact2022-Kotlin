package frc.robot.utils

import java.util.TreeMap

class InterpolatingTreeMap<K, V>(private val maxSize: Int = 0) :
  TreeMap<K, V>() where K : InverseInterpolable<K>, K : Comparable<K>, V : Interpolable<V> {
  override fun put(key: K, value: V): V? {
    if (maxSize in 1..size) {
      remove(firstKey())
    }
    return super.put(key, value)
  }

  override operator fun get(key: K): V? {
    val value = get(key)
    return if (value != null) value else {
      val lowerKey = floorKey(key)
      val upperKey = ceilingKey(key)

      if ((lowerKey === null) && (upperKey === null)) {
        return null
      } else if (lowerKey === null) {
        return get(upperKey)
      } else if (upperKey === null) {
        return get(lowerKey)
      }

      val lowerValue = get(lowerKey)
      val upperValue = get(upperKey)
      return upperValue?.let { lowerValue?.interpolate(it, lowerKey.inverseInterpolate(upperKey, key)) }
    }
  }
}