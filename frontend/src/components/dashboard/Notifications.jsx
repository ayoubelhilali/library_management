function Notifications({ activeBorrows, books }) {
  const getBookTitle = (bookId) => {
    const book = books.find((b) => b.id === bookId);
    return book ? book.title : `Book #${bookId}`;
  };

  if (activeBorrows.length === 0) {
    return <p className="text-slate-400">No notifications.</p>;
  }

  return (
    <div className="space-y-4">
      {activeBorrows.map((borrow) => {
        const bookId = borrow.bookID || borrow.bookId;

        return (
          <div
            key={borrow.id}
            className="bg-slate-900 border border-slate-800 p-5 rounded-2xl"
          >
            <h3 className="font-semibold text-yellow-400">Return reminder</h3>

            <p className="text-slate-300 mt-2">
              You should return{" "}
              <span className="text-indigo-400">{getBookTitle(bookId)}</span>{" "}
              before{" "}
              <span className="text-indigo-400">
                {borrow.expectedReturnDate}
              </span>
              .
            </p>
          </div>
        );
      })}
    </div>
  );
}

export default Notifications;
