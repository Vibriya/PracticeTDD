package com.github.johnnysc.practicetdd

interface MyStack<T : Any> {
    fun pop(): T
    fun push(item: T)

    @Suppress("UNCHECKED_CAST")
    class LIFO<T : Any>(private val maxCount: Int) : MyStack<T> {
        init {
            if (maxCount <= 0)  throw IllegalStateException()
        }
        private var count = 0
        private val _array = Array<Any>(maxCount) {}

        override fun pop(): T {
            if (count == 0) throw IllegalStateException()
            count--
            val item = _array[count]
            _array[count] = Any()
            return item as T
        }

        override fun push(item: T) {
            if (count == maxCount)
                throw IllegalStateException("Stack overflow exception, maximum is $maxCount")
            _array[count] = item
            count++
        }

    }

    @Suppress("UNCHECKED_CAST")
    class FIFO<T : Any>(private val maxCount: Int) : MyStack<T> {
        init {
            if (maxCount <= 0)  throw IllegalStateException()
        }
        private var count = 0
        private val _array = Array<Any>(maxCount) {}

        override fun pop(): T {
            if (count == 0) throw IllegalStateException()
            count--
            val item = _array[count]
            _array[count] = Any()
            return item as T
        }

        override fun push(item: T) {
            if (count == maxCount)
                throw IllegalStateException("Stack overflow exception, maximum is $maxCount")
            _array[maxCount - count - 1] = item
            count++
        }

    }

}
