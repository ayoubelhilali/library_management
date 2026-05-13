function BorrowList({ borrows, books, onReturnBook }) {
  const getBookTitle = (bookId) => {
    const book = books.find((b) => b.id === bookId);
    return book ? book.title : `Book #${bookId}`;
  };

  if (borrows.length === 0) {
    return (
      <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6 text-slate-400">
        No borrowed books found.
      </div>
    );
  }

  return (
    <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
      {borrows.map((borrow) => {
        const bookId = borrow.bookID || borrow.bookId;
        const isReturned = !!borrow.actualReturnDate;

        return (
          <div
            key={borrow.id}
            className="p-5 border-b border-slate-800 flex justify-between items-center"
          >
            <div>
              <h3 className="font-semibold">{getBookTitle(bookId)}</h3>

              <p className="text-slate-400 text-sm mt-1">
                Borrowed: {borrow.borrowDate}
              </p>

              <p className="text-slate-400 text-sm">
                Expected return:{" "}
                <span className="text-indigo-400">
                  {borrow.expectedReturnDate}
                </span>
              </p>
            </div>

            <div className="flex items-center gap-3">
              <span
                className={`px-3 py-1 rounded-full text-sm ${
                  isReturned
                    ? "bg-green-500/20 text-green-400"
                    : "bg-yellow-500/20 text-yellow-400"
                }`}
              >
                {isReturned ? "Returned" : "Active"}
              </span>

              {!isReturned && onReturnBook && (
                <button
                  onClick={() => onReturnBook(borrow.id)}
                  className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-1.5 rounded-lg text-sm font-medium transition-colors"
                >
                  Return Book
                </button>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}

export default BorrowList;
