import { createAsyncThunk } from "@reduxjs/toolkit";
import { transactionsService } from "../data/services/transactionsService";

export const fetchTransactions = createAsyncThunk(
    "transactions/fetchAll",
    async (_, { rejectWithValue }) => {
        try {
            return await transactionsService.getAll();
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

export const fetchTransactionById = createAsyncThunk(
    "transactions/fetchById",
    async (id, { rejectWithValue }) => {
        try {
            return await transactionsService.getById(id);
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

export const fetchBalance = createAsyncThunk(
    "transactions/fetchBalance",
    async (_, { rejectWithValue }) => {
        try {
            return await transactionsService.getBalance();
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

export const fetchByType = createAsyncThunk(
    "transactions/fetchByType",
    async (type, { rejectWithValue }) => {
        try {
            return await transactionsService.getByType(type);
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

export const fetchGrouped = createAsyncThunk(
    "transactions/fetchGrouped",
    async (_, { rejectWithValue }) => {
        try {
            return await transactionsService.getGrouped();
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

export const addTransaction = createAsyncThunk(
    "transactions/add",
    async (dto, { rejectWithValue }) => {
        try {
            return await transactionsService.create(dto);
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

export const updateTransaction = createAsyncThunk(
    "transactions/update",
    async ({ id, dto }, { rejectWithValue }) => {
        try {
            return await transactionsService.update(id, dto);
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);

export const removeTransaction = createAsyncThunk(
    "transactions/delete",
    async (id, { rejectWithValue }) => {
        try {
            return await transactionsService.remove(id);
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
);