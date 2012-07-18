package org.codesmell.wicket.tagcloud;

import java.util.Collection;

final class Assert
{
    private Assert()
    {
        // hide
    }

    public static final void isNotNull(final Object reference)
    {
        Assert.isNotNull(reference, "Reference was null");
    }

    public static final void isNotNull(final Object reference, final String msg)
    {
        if (reference == null)
        {
            Assert.raiseError(msg);
        }
    }

    public static final void isNotNullAndNotEmpty(final String reference)
    {
        Assert.isNotNullAndNotEmpty(reference, "Reference was null or empty");
    }

    public static final void isNotNullAndNotEmpty(final String reference, final String msg)
    {
        if ((reference == null) || (reference.length() == 0))
        {
            Assert.raiseError(msg);
        }
    }

    public static final void isFalse(final boolean value)
    {
        Assert.isFalse(value, "Value was true");
    }

    public static final void isFalse(final boolean value, final String msg)
    {
        if (value)
        {
            Assert.raiseError(msg);
        }
    }

    public static final void isNotFalse(final boolean value)
    {
        Assert.isNotFalse(value, "Value was false");
    }

    public static final void isNotFalse(boolean value, final String msg)
    {
        if (!value)
        {
            Assert.raiseError(msg);
        }
    }

    public static final void isTrue(final boolean value)
    {
        Assert.isTrue(value, "Value was false");
    }

    public static final void isTrue(boolean value, final String msg)
    {
        if (!value)
        {
            Assert.raiseError(msg);
        }
    }

    public static final void isNotTrue(final boolean value)
    {
        Assert.isNotTrue(value, "Value was true");
    }

    public static final void isNotTrue(final boolean value, final String msg)
    {
        if (value)
        {
            Assert.raiseError(msg);
        }
    }

    public static final void raiseError(final String error)
    {
        throw new AssertionFailedException(error);
    }

    public static final void raiseError(final String error, final Exception e)
    {
        throw new AssertionFailedException(error, e);
    }

    private static class AssertionFailedException extends RuntimeException
    {
        /**
         * Generated serial version UID for this class.
         */
        private static final long serialVersionUID = 435272532743543854L;

        /**
         * Constructs a new exception.
         */
        public AssertionFailedException()
        {
        }

        /**
         * Constructs a new exception with the given message.
         */
        public AssertionFailedException(final String detail)
        {
            super(detail);
        }

        public AssertionFailedException(final String detail, final Throwable e)
        {
            super(detail, e);
        }
    }

    public static void isEqual(final Object obj1, final Object obj2)
    {
        if ((obj1 == null) && (obj2 == null))
        {
            return;
        }
        if ((obj1 != null) && (!obj1.equals(obj2)))
        {
            Assert.raiseError("'" + obj1 + "' and '" + obj2 + "' are not equal");
        }
    }

    public static void isNotEmpty(final Collection<? extends Object> collection)
    {
        if (collection == null)
        {
            Assert.raiseError("'collection' is null");
        }
        else
            if (collection.isEmpty())
            {
                Assert.raiseError("'collection' is empty");
            }
    }

    public static void isEmpty(final Collection<? extends Object> collection)
    {
        if (collection == null)
        {
            Assert.raiseError("'collection' is null");
        }
        else
            if (!collection.isEmpty())
            {
                Assert.raiseError("'collection' is not empty");
            }
    }

    public static void parameterNotNull(final Object reference, final String nameOfParameter)
    {
        if (reference == null)
        {
            raiseError("Parameter '" + nameOfParameter + "' is not expected to be null.");
        }
    }

    public static void isNull(final Object object, final String string)
    {
        if (object != null)
        {
            raiseError(string);
        }
    }

    public static void parameterInRange(final int value, final int min, final int max, final String string)
    {
        isTrue((min <= value) && (value <= max), "Parameter '" + string + "' must be in range of " + min + " <= "
                + string + " <= " + max + ". Current value was " + value);

    }

    public static void parameterLegal(final boolean condition, final String parameter)
    {
        isTrue(condition, "Parameter '" + parameter + "' is not legal.");
    }

    public static void parameterIsEmail(final String email, final String string)
    {
        isTrue(isEmailAddress(email), "Parameter '" + string + "' has to be a valid Email-Address. Value was ('"
                + email + "').");
    }

    public static void isEmail(final String email, final String string)
    {
        isTrue(isEmailAddress(email), "'" + string + "' has to be a valid Email-Address. Value was ('" + email + "').");
    }

    private static boolean isEmailAddress(final String email)
    {
        return email.matches(".+@.+\\.[a-z]+");
    }

}
