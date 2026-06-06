import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  selectTransactions,
  selectStatus,
} from "../../store/transactionsSelectors";

import "./Dashboard.css";
import {
  fetchBalance,
  fetchTransactions,
  addTransaction,
  removeTransaction,
  updateTransaction,
} from "../../store/transactionsThunks";
import { Link } from "react-router-dom";

const TYPE_MAP = {
  0: "INCOME",
  1: "EXPENSE",
};

const Dashboard = () => {
  const dispatch = useDispatch();

  const transactions = useSelector(selectTransactions);
  const status = useSelector(selectStatus);

  const [filter, setFilter] = useState("all");
  const [searchTerm, setSearchTerm] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  const filteredTransactions = transactions.filter((t) => {
    const matchesFilter = filter === "all" || t.type === Number(filter);
    const matchesSearch = t.name.toLowerCase().includes(searchTerm.toLowerCase()) || 
                          t.category.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  const displayBalance = filteredTransactions.reduce((acc, t) => {
    if (filter === "all") {
      return t.type === 0 ? acc + t.amount : acc - t.amount;
    }
    return acc + t.amount;
  }, 0);

  useEffect(() => {
    if (status === "idle") {
      dispatch(fetchTransactions());
      dispatch(fetchBalance());
    }
  }, [status, dispatch]);

  const handleOpenEdit = (transaction) => {
    setEditingItem(transaction);
    setIsModalOpen(true);
  };

  const handleDelete = (id) => {
    if (window.confirm("Are you sure you want to delete this transaction?")) {
      dispatch(removeTransaction(id));
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingItem(null);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
      name: formData.get("name"),
      amount: Number(formData.get("amount")),
      type: Number(formData.get("type")),
      category: formData.get("category"),
      paymentMethod: formData.get("paymentMethod"),
      description: formData.get("description"),
      date: editingItem ? editingItem.date : new Date().toISOString(),
    };

    if (editingItem) {
      dispatch(updateTransaction({ id: editingItem.id, dto: data }));
    } else {
      dispatch(addTransaction(data));
    }
    closeModal();
  };

  if (status === "loading") return <p className="center">Loading...</p>;

  return (
    <div className="dashboard">
      <div className="top-bar">
  <div className="top-bar-left">
    <button className="add-btn" onClick={() => setIsModalOpen(true)}>
      + Add Transaction
    </button>
  </div>

  <div className="controls">
    <input 
      type="text" 
      placeholder="Search by name or category..." 
      className="search-input"
      value={searchTerm}
      onChange={(e) => setSearchTerm(e.target.value)}
    />
    
    <div className="filter-container">
      <label>View: </label>
      <select value={filter} onChange={(e) => setFilter(e.target.value)}>
        <option value="all">None (All)</option>
        <option value="0">Incomes Only</option>
        <option value="1">Expenses Only</option>
      </select>
    </div>
  </div>
</div>

      <div className="balance-card">
        <h2>
          {filter === "all"
            ? "Total Balance"
            : filter === "0"
              ? "Total Income"
              : "Total Expenses"}
        </h2>
        <h1
          className={
            (filter === "all" && displayBalance < 0) || filter === "1"
              ? "negative"
              : "positive"
          }
        >
          {filter === "1" || (filter === "all" && displayBalance < 0)
            ? "-"
            : ""}
          ${Math.abs(displayBalance).toFixed(2)}
        </h1>
      </div>

      {filteredTransactions.length === 0 ? (
        <p className="center">
          No transactions yet. Click the button to add one
        </p>
      ) : (
        <div className="transactions-list">
          {filteredTransactions.map((t) => (
            <div
              key={t.id}
              className={`transaction-card ${
                t.type === 0 ? "income" : "expense"
              }`}
            >
              <div className="transaction-header">
                <h3>{t.name}</h3>
                <div className="header-right">
                  <span className="amount">
                    {t.type === 0 ? "+" : "-"}${t.amount.toFixed(2)}
                  </span>
                  <div className="actions">
                    <button
                      className="edit-icon"
                      onClick={() => handleOpenEdit(t)}
                    >
                      Edit
                    </button>
                    <button
                      className="delete-icon"
                      onClick={() => handleDelete(t.id)}
                    >
                      X
                    </button>
                  </div>
                </div>
              </div>

              <div className="transaction-body">
                <p>
                  <strong>Description:</strong> {t.description || "—"}
                </p>
                <p>
                  <strong>Category:</strong> {t.category}
                </p>
                <p>
                  <strong>Payment:</strong> {t.paymentMethod || "—"}
                </p>
                <p>
                  <strong>Date:</strong> {new Date(t.date).toLocaleDateString()}
                </p>
                <p>
                  <strong>Type:</strong> {TYPE_MAP[t.type] || t.type}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editingItem ? "Edit Transaction" : "Add Transaction"}</h2>
            <form onSubmit={handleSubmit}>
              <input
                name="name"
                defaultValue={editingItem?.name}
                placeholder="Name"
                required
              />
              <input
                name="amount"
                type="number"
                step="0.01"
                defaultValue={editingItem?.amount}
                placeholder="Amount"
                required
              />
              <select
                name="type"
                defaultValue={editingItem?.type ?? 0}
                required
              >
                <option value="0">Income</option>
                <option value="1">Expense</option>
              </select>
              <input
                name="category"
                defaultValue={editingItem?.category}
                placeholder="Category"
                required
              />
              <input
                name="paymentMethod"
                defaultValue={editingItem?.paymentMethod}
                placeholder="Payment Method"
              />
              <textarea
                name="description"
                defaultValue={editingItem?.description}
                placeholder="Description"
              />
              <div className="modal-actions">
                <button type="submit" className="save-btn">
                  Save
                </button>
                <button type="button" onClick={closeModal}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;
