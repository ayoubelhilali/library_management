import StatCard from "../components/dashboard/StatCard";
import BorrowList from "../components/dashboard/BorrowList";

function OverviewPage({ books, borrows, user }) {
  const activeBorrows = borrows.filter((b) => !b.actualReturnDate);
  const returnedBorrows = borrows.filter((b) => b.actualReturnDate);

  const availableBooks = books.filter((book) => book.status === "AVAILABLE");

  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <StatCard title="Total Books" value={books.length} icon="📚" />

        <StatCard
          title="Available Books"
          value={availableBooks.length}
          icon="✅"
        />

        <StatCard
          title={user.role === "ADMIN" ? "Total Borrows" : "My Borrowed Books"}
          value={activeBorrows.length}
          icon="🔄"
        />

        <StatCard
          title="Returned Books"
          value={returnedBorrows.length}
          icon="↩️"
        />
      </div>

      <h2 className="text-2xl font-bold mb-5">Recent Activity</h2>

      <BorrowList borrows={borrows.slice(0, 5)} books={books} />
    </>
  );
}

export default OverviewPage;
