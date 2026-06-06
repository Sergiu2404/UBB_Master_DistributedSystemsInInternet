import { configureStore } from "@reduxjs/toolkit";
import transactionsReducer from "../store/transactionsSlice";

export const transactionStore = configureStore({
    reducer: {
        transactions: transactionsReducer
    }
});