package com.portalsoup.mrbutlertron.data.util

import com.google.common.base.Preconditions
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * Helper methods for class type parameters.
 * @see [Super Type Tokens](http://gafter.blogspot.com/2006/12/super-type-tokens.html)
 *
 * This is just here to make the AbstractDao not depend on dropwizard.
 */
object Generics {
    /**
     * Finds the type parameter for the given class.
     *
     * @param klass a parameterized class
     * @return the class's type parameter
     */
    fun getTypeParameter(klass: Class<*>): Class<*> {
        return getTypeParameter(klass, Any::class.java)
    }

    /**
     * Finds the type parameter for the given class which is assignable to the bound class.
     *
     * @param klass a parameterized class
     * @param bound the type bound
     * @param <T>   the type bound
     * @return the class's type parameter
    </T> */
    fun <T> getTypeParameter(klass: Class<*>, bound: Class<in T>): Class<T> {
        var t: Type? = Preconditions.checkNotNull(klass)
        while (t is Class<*>) {
            t = t.genericSuperclass
        }
        /* This is not guaranteed to work for all cases with convoluted piping
         * of type parameters: but it can at least resolve straight-forward
         * extension with single type parameter (as per [Issue-89]).
         * And when it fails to do that, will indicate with specific exception.
         */if (t is ParameterizedType) { // should typically have one of type parameters (first one) that matches:
            for (param in t.actualTypeArguments) {
                if (param is Class<*>) {
                    val cls = determineClass(bound, param)
                    if (cls != null) {
                        return cls
                    }
                } else if (param is TypeVariable<*>) {
                    for (paramBound in param.bounds) {
                        if (paramBound is Class<*>) {
                            val cls = determineClass(bound, paramBound)
                            if (cls != null) {
                                return cls
                            }
                        }
                    }
                }
            }
        }
        throw IllegalStateException("Cannot figure out type parameterization for " + klass.name)
    }

    private fun <T> determineClass(
        bound: Class<in T>,
        candidate: Type
    ): Class<T>? {
        if (candidate is Class<*>) {
            val cls = candidate
            if (bound.isAssignableFrom(cls)) {
                return cls as Class<T>
            }
        }
        return null
    }
}