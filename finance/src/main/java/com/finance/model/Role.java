package com.finance.model;

/**
 * VIEWER  – read dashboard data only.
 * ANALYST – read data + access analytics.
 * ADMIN   – full CRUD on records and user management.
 */
public enum Role { VIEWER, ANALYST, ADMIN }