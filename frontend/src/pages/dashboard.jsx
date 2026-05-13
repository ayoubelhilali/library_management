import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/api";

import Sidebar from "../components/dashboard/SideBar";

import OverviewPage from "./overviewPage";
import BrowseBooksPage from "./member/browseBooksPage";
import BorrowedBooksPage from "./member/borrowedBooksPage";
import ReservationsPage from "./member/reservationsPage";
import NotificationsPage from "./notificationsPage";
import ProfilePage from "./profilePage";
import AdminBooksPage from "./admin/adminBooksPage";
import AdminMembersPage from "./admin/adminMembersPage";

function Dashboard() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [books, setBooks] = useState([]);
  const [borrows, setBorrows] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [activeTab, setActiveTab] = useState("overview");

  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (!storedUser) {
      navigate("/login");
      return;
    }

    const parsedUser = JSON.parse(storedUser);
    setUser(parsedUser);
    loadData(parsedUser);
  }, []);

  const loadData = async (currentUser) => {
    try {
      const booksRes = await API.get("/books");
      const borrowsRes = await API.get("/borrows");

      setBooks(booksRes.data);

      if (currentUser.role === "MEMBER") {
        const userBorrows = borrowsRes.data.filter(
          (b) => b.memberID === currentUser.id || b.memberId === currentUser.id,
        );

        const reservationsRes = await API.get(
          `/reservations?memberId=${currentUser.id}`,
        );

        setBorrows(userBorrows);
        setReservations(reservationsRes.data);
      } else {
        setBorrows(borrowsRes.data);
      }
    } catch (err) {
      console.error("Loading dashboard failed:", err);
    }
  };

  const logout = () => {
    localStorage.removeItem("user");
    navigate("/login");
  };

  if (!user) {
    return (
      <div className="min-h-screen bg-slate-950 text-white flex items-center justify-center">
        Loading...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-950 text-white">
      <Sidebar
        user={user}
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        logout={logout}
      />

      <main className="md:ml-72 p-8 min-h-screen">
        <div className="mb-8">
          <h1 className="text-4xl font-bold">Welcome, {user.username}</h1>
          <p className="text-slate-400 mt-2">Role: {user.role}</p>
        </div>

        {activeTab === "overview" && (
          <OverviewPage books={books} borrows={borrows} user={user} />
        )}

        {activeTab === "browse" && (
          <BrowseBooksPage
            user={user}
            books={books}
            borrows={borrows}
            reservations={reservations}
            reload={() => loadData(user)}
            setActiveTab={setActiveTab}
          />
        )}

        {activeTab === "borrows" && (
          <BorrowedBooksPage
            books={books}
            borrows={borrows}
            reload={() => loadData(user)}
          />
        )}

        {activeTab === "reservations" && (
          <ReservationsPage books={books} reservations={reservations} />
        )}

        {activeTab === "notifications" && <NotificationsPage user={user} />}

        {activeTab === "profile" && <ProfilePage user={user} />}

        {activeTab === "adminBooks" && (
          <AdminBooksPage books={books} reload={() => loadData(user)} />
        )}

        {activeTab === "adminMembers" && <AdminMembersPage />}
      </main>
    </div>
  );
}

export default Dashboard;
