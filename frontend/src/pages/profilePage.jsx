import ProfileCard from "../components/dashboard/ProfileCard";

function ProfilePage({ user }) {
  return (
    <>
      <h2 className="text-2xl font-bold mb-5">Profile Information</h2>
      <ProfileCard user={user} />
    </>
  );
}

export default ProfilePage;
