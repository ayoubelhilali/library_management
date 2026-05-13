import React, { useState } from "react";
import {
  History,
  Search,
  Calendar,
  User,
  Book as BookIcon,
  CheckCircle2,
  Clock,
  Filter,
} from "lucide-react";

function AdminBorrowsPage({ books, borrows, members }) {
  const [searchQuery, setSearchQuery] = useState("");
  const [filterStatus, setFilterStatus] = useState("all"); // all, active, returned

  const getBookTitle = (bookId) => {
    const book = books.find((b) => b.id === bookId);
    return book ? book.title : `Book #${bookId}`;
  };

  const getMemberName = (memberId) => {
    const member = members.find((m) => m.id === memberId);
    return member ? member.username : `Member #${memberId}`;
  };

  // Filter Logic
  const filteredBorrows = borrows.filter((borrow) => {
    const bookId = borrow.bookID || borrow.bookId;
    const memberId = borrow.memberID || borrow.memberId;
    const bookTitle = getBookTitle(bookId).toLowerCase();
    const memberName = getMemberName(memberId).toLowerCase();
    const isReturned = !!borrow.actualReturnDate;

    const matchesSearch =
      bookTitle.includes(searchQuery.toLowerCase()) ||
      memberName.includes(searchQuery.toLowerCase());

    if (filterStatus === "active") return matchesSearch && !isReturned;
    if (filterStatus === "returned") return matchesSearch && isReturned;
    return matchesSearch;
  });

  return (
    <div className="max-w-6xl mx-auto">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
        <div>
          <h2 className="text-3xl font-bold text-white flex items-center gap-3">
            <History className="text-indigo-400" />
            Borrowing History
          </h2>
          <p className="text-slate-400 mt-1">
            Track and manage all book circulations.
          </p>
        </div>

        <div className="flex bg-slate-900 p-1 rounded-xl border border-slate-800">
          {["all", "active", "returned"].map((status) => (
            <button
              key={status}
              onClick={() => setFilterStatus(status)}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-all capitalize ${
                filterStatus === status
                  ? "bg-indigo-600 text-white shadow-lg"
                  : "text-slate-400 hover:text-slate-200"
              }`}
            >
              {status}
            </button>
          ))}
        </div>
      </div>

      {/* Search Bar */}
      <div className="relative mb-6">
        <Search
          className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500"
          size={20}
        />
        <input
          type="text"
          placeholder="Search by book title or member name..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full bg-slate-900 border border-slate-800 rounded-2xl py-4 pl-12 pr-4 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 transition-all"
        />
      </div>

      {/* List Container */}
      <div className="space-y-4">
        {filteredBorrows.length === 0 ? (
          <div className="bg-slate-900/50 border border-dashed border-slate-800 rounded-3xl p-12 text-center">
            <Filter className="text-slate-600 mx-auto mb-4" size={40} />
            <p className="text-slate-400 text-lg">
              No borrow records match your criteria.
            </p>
          </div>
        ) : (
          filteredBorrows.map((borrow) => {
            const bookId = borrow.bookID || borrow.bookId;
            const memberId = borrow.memberID || borrow.memberId;
            const isReturned = !!borrow.actualReturnDate;

            return (
              <div
                key={borrow.id}
                className="group bg-slate-900 border border-slate-800 hover:border-slate-700 p-5 rounded-2xl flex flex-col md:flex-row md:items-center justify-between gap-6 transition-all"
              >
                <div className="flex items-start gap-4">
                  <div
                    className={`h-12 w-12 rounded-xl flex items-center justify-center shrink-0 ${
                      isReturned
                        ? "bg-green-500/10 text-green-500"
                        : "bg-amber-500/10 text-amber-500"
                    }`}
                  >
                    {isReturned ? (
                      <CheckCircle2 size={24} />
                    ) : (
                      <Clock size={24} />
                    )}
                  </div>

                  <div>
                    <h3 className="text-lg font-bold text-white group-hover:text-indigo-400 transition-colors">
                      {getBookTitle(bookId)}
                    </h3>

                    <div className="flex flex-wrap items-center gap-y-2 gap-x-6 mt-2 text-sm">
                      <span className="flex items-center gap-2 text-slate-300">
                        <User size={14} className="text-indigo-400" />
                        {getMemberName(memberId)}
                      </span>
                      <span className="flex items-center gap-2 text-slate-400">
                        <Calendar size={14} />
                        {borrow.borrowDate}
                        <span className="text-slate-600">→</span>
                        {borrow.expectedReturnDate}
                      </span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center justify-between md:justify-end gap-4 border-t md:border-t-0 border-slate-800 pt-4 md:pt-0">
                  <div className="text-right hidden md:block">
                    <p className="text-[10px] uppercase font-bold tracking-widest text-slate-500 mb-1">
                      Status
                    </p>
                    <span
                      className={`px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider ${
                        isReturned
                          ? "bg-green-500/10 text-green-400 border border-green-500/20"
                          : "bg-amber-500/10 text-amber-400 border border-amber-500/20"
                      }`}
                    >
                      {isReturned ? "Returned" : "On Loan"}
                    </span>
                  </div>

                  {/* Mobile Badge Only */}
                  <span
                    className={`md:hidden px-3 py-1 rounded-full text-xs font-bold uppercase ${
                      isReturned
                        ? "bg-green-500/10 text-green-400"
                        : "bg-amber-500/10 text-amber-400"
                    }`}
                  >
                    {isReturned ? "Returned" : "Active"}
                  </span>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

export default AdminBorrowsPage;
