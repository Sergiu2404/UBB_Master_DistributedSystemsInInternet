export const selectTransactions = state => state.transactions.items;
export const selectBalance = state => state.transactions.balance;
export const selectGrouped = state => state.transactions.grouped;
export const selectStatus = state => state.transactions.status;
export const selectError = state => state.transactions.error;
export const selectSelected = state => state.transactions.selected;