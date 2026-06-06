import { createSlice } from "@reduxjs/toolkit";
import {
  fetchTransactions,
  fetchTransactionById,
  fetchBalance,
  // fetchByType,
  fetchGrouped,
  addTransaction,
  updateTransaction,
  removeTransaction,
} from "./transactionsThunks";

const initialState = {
  items: [],
  selected: null,
  balance: 0,
  grouped: {},
  status: "idle",
  error: null,
};

const transactionsSlice = createSlice({
  name: "transactions",
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearSelected: (state) => {
      state.selected = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchTransactions.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchTransactions.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.items = action.payload;
      })
      .addCase(fetchTransactions.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })

      .addCase(fetchTransactionById.fulfilled, (state, action) => {
        state.selected = action.payload;
      })

      .addCase(fetchBalance.fulfilled, (state, action) => {
        state.balance = action.payload;
      })

      .addCase(fetchGrouped.fulfilled, (state, action) => {
        state.grouped = action.payload;
      })

      .addCase(addTransaction.fulfilled, (state, action) => {
        state.items.push(action.payload);

        // update balance too without recalling balance
        const amount = action.payload.amount;

        if (action.payload.type === 0) {
          state.balance += amount;
        } else {
          state.balance -= amount;
        }
      })

      .addCase(updateTransaction.fulfilled, (state, action) => {
        const index = state.items.findIndex((t) => t.id === action.payload.id);
        if (index !== -1) {
          const oldItem = state.items[index];
          const newItem = action.payload;

          // subtract old amount
          state.balance +=
            oldItem.type === 0 ? -oldItem.amount : oldItem.amount;

          // add new amount
          state.balance +=
            newItem.type === 0 ? newItem.amount : -newItem.amount;

          state.items[index] = newItem;
        }
      })

      .addCase(removeTransaction.fulfilled, (state, action) => {
        const id = action.payload;
        const itemToRemove = state.items.find((t) => t.id === id);

        if (itemToRemove) {
          // at delete, subtract deleted amount
          state.balance +=
            itemToRemove.type === 0
              ? -itemToRemove.amount
              : itemToRemove.amount;
        }

        state.items = state.items.filter((t) => t.id !== id);
      });
  },
});

export const { clearError, clearSelected } = transactionsSlice.actions;
export default transactionsSlice.reducer;
