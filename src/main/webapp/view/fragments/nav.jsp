<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<nav class="bg-white shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
            <div class="flex items-center">
                <div class="flex-shrink-0 flex items-center">
                    <i class="fas fa-heartbeat text-accent text-2xl mr-2"></i>
                    <span class="text-xl font-bold text-gray-800">SangLink</span>
                </div>
                <div class="hidden md:ml-6 md:flex md:space-x-8">
                    <a href="<%= contextPath %>/" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">Home</a>
                    <a href="#" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">About</a>
                    <a href="#" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">Contact</a>

                    <!-- Donor Links -->
                    <div class="relative group">
                        <a href="#" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">
                            Donors <i class="fas fa-chevron-down ml-1 text-xs"></i>
                        </a>
                        <div class="absolute left-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300">
                            <a href="<%= contextPath %>/donor" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Donor List</a>
                            <a href="<%= contextPath %>/donor/create" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Create Donor</a>
                        </div>
                    </div>

                    <!-- Receiver Links -->
                    <div class="relative group">
                        <a href="#" class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">
                            Receivers <i class="fas fa-chevron-down ml-1 text-xs"></i>
                        </a>
                        <div class="absolute left-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300">
                            <a href="<%= contextPath %>/receiver" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Receiver List</a>
                            <a href="<%= contextPath %>/receiver/create" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Create Receiver</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="flex items-center space-x-4">
                <a href="<%= contextPath %>/donor/create" class="bg-primary hover:bg-secondary text-white px-4 py-2 rounded-md text-sm font-medium transition duration-300">
                    <i class="fas fa-user-plus mr-1"></i> Register Donor
                </a>
                <a href="<%= contextPath %>/receiver/create" class="bg-accent hover:bg-accent-dark text-white px-4 py-2 rounded-md text-sm font-medium transition duration-300">
                    <i class="fas fa-hand-holding-medical mr-1"></i> Register Receiver
                </a>
            </div>
        </div>
    </div>
</nav>