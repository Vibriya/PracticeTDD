package com.github.johnnysc.practicetdd

import android.content.res.Resources

interface Parser {
    fun parse(raw: String): List<Any>

    class Base(private val delimiter: String) : Parser {
        init {
            if (delimiter.isEmpty()) throw IllegalStateException()
        }

        override fun parse(raw: String): List<Any> {
            if (raw.isEmpty()) return emptyList()
            val resultList = mutableListOf<Any>()
            val resultItem = StringBuilder()
            var positionInDelimiter = 0
            raw.forEachIndexed { index, c ->
                if (c != delimiter[positionInDelimiter]) {
                    resultItem.append(c)
                    if (positionInDelimiter != 0 || index == raw.length - 1) {
                        resultList.add(resultItem.toString())
                        positionInDelimiter = 0
                        resultItem.clear()
                    }
                } else {
                    if (positionInDelimiter < delimiter.length - 1)
                        positionInDelimiter++
                    else {
                        resultList.add(resultItem.toString())
                        resultItem.clear()
                    }

                }
            }

            if (resultList.size == 1 && resultList[0].toString().isEmpty()) return emptyList()

            for (index in 0 until resultList.size) {
                val item = resultList[index].toString()

                val byteItem = item.toByteOrNull()
                if (byteItem != null) {
                    resultList[index] = byteItem
                    continue
                }

                val shortItem = item.toShortOrNull()
                if (shortItem != null) {
                    resultList[index] = shortItem
                    continue
                }

                val intItem = item.toIntOrNull()
                if (intItem != null) {
                    resultList[index] = intItem
                    continue
                }

                val longItem = item.toLongOrNull()
                if (longItem != null) {
                    resultList[index] = longItem
                    continue
                }

                val doubleItem = item.toDoubleOrNull()
                if (doubleItem != null && !doubleItem.isInfinite()) {
                    resultList[index] =
                        if (item.substringBefore('.').length < FLOAT_FRONTIER)
                            try {
                                item.toFloat()
                            } catch (e: Exception) {
                                throw IllegalStateException(
                                    String.format(
                                        Resources.getSystem().getString(R.string.parse_float_error),
                                        item
                                    )
                                )
                            }
                        else
                            doubleItem
                    continue
                }

                val charItem = item.singleOrNull()
                if (charItem != null) {
                    resultList[index] = charItem
                    continue
                }

                val boolItem = item.toBooleanStrictOrNull()
                if (boolItem != null) {
                    resultList[index] = boolItem
                    continue
                }
            }
            return resultList
        }

    }

    companion object {
        private const val FLOAT_FRONTIER: Byte = 20
    }
}
