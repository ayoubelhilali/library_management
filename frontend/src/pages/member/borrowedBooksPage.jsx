import API from "../../api/api";
import BorrowList from "../../components/dashboard/BorrowList";

function BorrowedBooksPage({ books, borrows, reload }) {
  const activeBorrows = borrows.filter((b) => !b.actualReturnDate);
  const returnedBorrows = borrows.filter((b) => b.actualReturnDate);

  const handleReturnBook = async (borrowId) => {
    try {
      await API.post("/return", { borrowId });
      alert("Book returned successfully");
      await reload();
    } catch (err) {
      alert(err.response?.data?.error || "Return failed");
    }
  };

  return (
    <>
      <h2 className="text-2xl font-bold mb-5">My Borrowed Books</h2>
      <BorrowList
        borrows={activeBorrows}
        books={books}
        onReturnBook={handleReturnBook}
      />

      <h2 className="text-2xl font-bold mt-10 mb-5">Returned Books</h2>
      <BorrowList borrows={returnedBorrows} books={books} />
    </>
  );
}

export default BorrowedBooksPage;
