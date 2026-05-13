import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/api";

import Sidebar from "../components/dashboard/SideBar";

import OverviewPage from "./overviewPage";
import BrowseBooksPage from "./member/browseBooksPage";
import BorrowedBooksPage from "./member/borrowedBooksPage";
import ReservationsPage from "./member/reservationsPage";
import AdminBooksPage from "./admin/adminBooksPage";
import AdminMembersPage from "./admin/adminMembersPage";
import AdminBorrowsPage from "./admin/adminBorrowsPage";
import AdminReservationsPage from "./admin/adminReservationsPage";
import ProfilePage from "../components/profile";
import NotificationPage from "../components/notifications";


function Dashboard() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  const [books, setBooks] = useState([]);
  const [borrows, setBorrows] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [members, setMembers] = useState([]);
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
        const reservationsRes = await API.get("/reservations");
        const membersRes = await API.get("/members");

        setBorrows(borrowsRes.data);
        setReservations(reservationsRes.data);
        setMembers(membersRes.data);
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
        {activeTab === "overview" && (
          <OverviewPage
            books={books}
            borrows={borrows}
            reservations={reservations}
            members={members}
            user={user}
          />
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

        {activeTab === "borrows" &&
          (user.role === "ADMIN" ? (
            <AdminBorrowsPage
              books={books}
              borrows={borrows}
              members={members}
            />
          ) : (
            <BorrowedBooksPage
              books={books}
              borrows={borrows}
              reload={() => loadData(user)}
            />
          ))}

        {activeTab === "reservations" &&
          (user.role === "ADMIN" ? (
            <AdminReservationsPage
              books={books}
              reservations={reservations}
              members={members}
            />
          ) : (
            <ReservationsPage books={books} reservations={reservations} />
          ))}

        {/* {activeTab === "notifications" && <NotificationsPage user={user} />} */}
        {activeTab === "notifications" && <NotificationPage user={user} />}

        {/* {activeTab === "profile" && <ProfilePage user={user} />} */}
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
