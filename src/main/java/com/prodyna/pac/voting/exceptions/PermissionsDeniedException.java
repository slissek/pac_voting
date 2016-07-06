package com.prodyna.pac.voting.exceptions;

public class PermissionsDeniedException extends Exception
{
    private static final long serialVersionUID = 1L;

    public PermissionsDeniedException()
    {
        super();
    }

    public PermissionsDeniedException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public PermissionsDeniedException(final String message)
    {
        super(message);
    }
}
