import React, { useState } from "react";
import {
  Bookmark,
  Search,
  Calendar,
  User,
  Hash,
  Clock,
  CheckCircle,
  XCircle,
  Filter,
} from "lucide-react";

function AdminReservationsPage({ books, reservations, members }) {
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const getBookTitle = (bookId) => {
    const book = books.find((b) => b.id === bookId);
    return book ? book.title : `Book #${bookId}`;
  };

  const getMemberName = (memberId) => {
    const member = members.find((m) => m.id === memberId);
    return member ? member.username : `Member #${memberId}`;
  };

  const getStatusStyles = (status) => {
    switch (status?.toUpperCase()) {
      case "PENDING":
        return "bg-amber-500/10 text-amber-500 border-amber-500/20";
      case "COMPLETED":
        return "bg-green-500/10 text-green-500 border-green-500/20";
      case "CANCELED":
        return "bg-red-500/10 text-red-500 border-red-500/20";
      default:
        return "bg-indigo-500/10 text-indigo-400 border-indigo-500/20";
    }
  };

  const filteredReservations = reservations.filter((res) => {
    const bookTitle = getBookTitle(res.bookID || res.bookId).toLowerCase();
    const memberName = getMemberName(
      res.memberID || res.memberId,
    ).toLowerCase();
    const matchesSearch =
      bookTitle.includes(searchQuery.toLowerCase()) ||
      memberName.includes(searchQuery.toLowerCase());

    const matchesStatus = statusFilter === "ALL" || res.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  return (
    <div className="max-w-6xl mx-auto">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6 mb-8">
        <div>
          <h2 className="text-3xl font-bold text-white flex items-center gap-3">
            <Bookmark
              className="text-indigo-400"
              fill="currentColor"
              size={28}
            />
            Active Reservations
          </h2>
          <p className="text-slate-400 mt-2">
            Manage the waitlist and book hold requests.
          </p>
        </div>

        <div className="flex bg-slate-900 p-1 rounded-xl border border-slate-800">
          {["ALL", "PENDING", "COMPLETED"].map((s) => (
            <button
              key={s}
              onClick={() => setStatusFilter(s)}
              className={`px-4 py-2 rounded-lg text-xs font-bold transition-all ${
                statusFilter === s
                  ? "bg-indigo-600 text-white shadow-lg"
                  : "text-slate-500 hover:text-slate-300"
              }`}
            >
              {s}
            </button>
          ))}
        </div>
      </div>

      {/* Search Input */}
      <div className="relative mb-8">
        <Search
          className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-500"
          size={20}
        />
        <input
          type="text"
          placeholder="Search by book or member..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full bg-slate-900 border border-slate-800 rounded-2xl py-4 pl-12 pr-4 text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 transition-all placeholder:text-slate-600"
        />
      </div>

      {/* Reservations List */}
      <div className="grid gap-4">
        {filteredReservations.length === 0 ? (
          <div className="bg-slate-900/50 border border-dashed border-slate-800 rounded-3xl p-12 text-center">
            <Clock className="text-slate-700 mx-auto mb-4" size={48} />
            <p className="text-slate-400 text-lg">
              No reservations matching your filters.
            </p>
          </div>
        ) : (
          filteredReservations.map((res) => {
            const bookId = res.bookID || res.bookId;
            const memberId = res.memberID || res.memberId;

            return (
              <div
                key={res.reservationId}
                className="group bg-slate-900 border border-slate-800 hover:border-indigo-500/40 p-5 rounded-2xl flex flex-col md:flex-row md:items-center justify-between gap-6 transition-all duration-300"
              >
                <div className="flex items-start gap-5">
                  <div className="h-14 w-14 bg-indigo-500/5 rounded-2xl border border-indigo-500/10 flex flex-col items-center justify-center text-indigo-400 group-hover:bg-indigo-600 group-hover:text-white transition-all">
                    <span className="text-[10px] font-bold uppercase tracking-tighter leading-none mb-1">
                      Queue
                    </span>
                    <span className="text-xl font-black leading-none">
                      {res.queuePosition}
                    </span>
                  </div>

                  <div>
                    <h3 className="text-lg font-bold text-white group-hover:text-indigo-400 transition-colors">
                      {getBookTitle(bookId)}
                    </h3>

                    <div className="flex flex-wrap items-center gap-y-2 gap-x-6 mt-2 text-sm">
                      <span className="flex items-center gap-2 text-slate-300">
                        <User size={14} className="text-indigo-500" />
                        {getMemberName(memberId)}
                      </span>
                      <span className="flex items-center gap-2 text-slate-400">
                        <Calendar size={14} className="text-slate-600" />
                        {res.reservationDate}
                      </span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center justify-between md:justify-end gap-6 border-t md:border-t-0 border-slate-800 pt-4 md:pt-0">
                  <div className="flex flex-col items-end">
                    <span
                      className={`px-4 py-1.5 rounded-lg text-[11px] font-black uppercase tracking-widest border ${getStatusStyles(res.status)}`}
                    >
                      {res.status || "PENDING"}
                    </span>
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

export default AdminReservationsPage;
