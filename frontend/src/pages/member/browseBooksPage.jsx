import { useState } from "react";
import API from "../../api/api";

function BrowseBooksPage({
  user,
  books,
  borrows,
  reservations,
  reload,
  setActiveTab,
}) {
  const [searchTerm, setSearchTerm] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("All");

  const activeBorrows = borrows.filter((b) => !b.actualReturnDate);

  const filteredBooks = books.filter((book) => {
    const matchesSearch =
      book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      book.author.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory =
      categoryFilter === "All" || book.category === categoryFilter;
    return matchesSearch && matchesCategory;
  });

  const categories = ["All", ...new Set(books.map((b) => b.category))].filter(
    (c) => c,
  );

  const handleBorrowBook = async (bookId) => {
    try {
      await API.post("/borrow", {
        bookId,
        memberId: user.id,
      });

      await reload();

      setActiveTab("borrows");
    } catch (err) {
      alert(err.response?.data?.error || "Borrow failed");
    }
  };

  const handleReserveBook = async (bookId) => {
    try {
      await API.post("/reservations", {
        bookId,
        memberId: user.id,
      });

      alert("Book reserved successfully");

      await reload();
    } catch (err) {
      alert(err.response?.data?.error || "Reservation failed");
    }
  };

  return (
    <>
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-8">
        <h2 className="text-3xl font-bold">Browse Books</h2>

        <div className="flex flex-col sm:flex-row gap-3 w-full md:w-auto">
          <input
            type="text"
            placeholder="Search books..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="bg-slate-900 border border-slate-800 rounded-xl px-4 py-2 text-white"
          />
          <select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            className="bg-slate-900 border border-slate-800 rounded-xl px-4 py-2 text-white"
          >
            {categories.map((c) => (
              <option key={c} value={c}>
                {c}
              </option>
            ))}
          </select>
        </div>
      </div>

      {filteredBooks.length === 0 ? (
        <div className="bg-slate-900 border border-slate-800 p-12 rounded-2xl text-center">
          <p className="text-slate-400 text-lg">
            No books found matching your filters.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
          {filteredBooks.map((book) => {
            const isReservedByUser = reservations.some(
              (r) =>
                (r.bookID === book.id || r.bookId === book.id) &&
                r.status === "PENDING",
            );

            const isBorrowedByUser = activeBorrows.some(
              (b) => b.bookID === book.id || b.bookId === book.id,
            );

            return (
              <div
                key={book.id}
                className="bg-slate-900 border border-slate-800 p-6 rounded-2xl"
              >
                <h3 className="text-xl font-bold mb-2">{book.title}</h3>

                <p className="text-slate-400 mb-1">Author: {book.author}</p>

                <p className="text-slate-400 mb-1">Category: {book.category}</p>

                <p className="text-slate-400 mb-4">ISBN: {book.isbn}</p>

                <span
                  className={`inline-block px-3 py-1 rounded-full text-sm mb-4 ${
                    book.status === "AVAILABLE"
                      ? "bg-green-500/20 text-green-400"
                      : "bg-yellow-500/20 text-yellow-400"
                  }`}
                >
                  {book.status}
                </span>

                {book.status === "AVAILABLE" ? (
                  <button
                    onClick={() => handleBorrowBook(book.id)}
                    className="block w-full mt-4 bg-indigo-600 hover:bg-indigo-700 py-2 rounded-lg"
                  >
                    Borrow Book
                  </button>
                ) : isReservedByUser ? (
                  <button
                    disabled
                    className="block w-full mt-4 bg-slate-700 text-slate-400 py-2 rounded-lg"
                  >
                    Already Reserved
                  </button>
                ) : isBorrowedByUser ? (
                  <button
                    disabled
                    className="block w-full mt-4 bg-slate-700 text-slate-400 py-2 rounded-lg"
                  >
                    Currently Borrowed
                  </button>
                ) : (
                  <button
                    onClick={() => handleReserveBook(book.id)}
                    className="block w-full mt-4 bg-yellow-600 hover:bg-yellow-700 py-2 rounded-lg"
                  >
                    Reserve Book
                  </button>
                )}
              </div>
            );
          })}
        </div>
      )}
    </>
  );
}

export default BrowseBooksPage;
