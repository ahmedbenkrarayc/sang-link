<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="bg-white shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
            <!-- Left section (Logo + Menu) -->
            <div class="flex items-center">
                <div class="flex-shrink-0 flex items-center">
                    <i class="fas fa-heartbeat text-accent text-2xl mr-2"></i>
                    <span class="text-xl font-bold text-gray-800">SangLink</span>
                </div>
                <div class="hidden md:ml-6 md:flex md:space-x-8">
                    <a href="<c:url value='/donor'/>"
                       class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium"">
                        Donors
                    </a>

                    <a href="<c:url value='/donor/create'/>"
                       class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">
                        Create donor
                    </a>

                    <a href="<c:url value='/receiver'/>"
                       class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">
                        Recipients
                    </a>

                    <a href="<c:url value='/receiver/create'/>"
                       class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 text-sm font-medium">
                        Create recipient
                    </a>
                </div>
            </div>

            <!-- Right section (Register button) -->
            <div class="flex items-center">
                <button class="bg-primary hover:bg-secondary text-white px-4 py-2 rounded-md text-sm font-medium transition duration-300">
                    <i class="fas fa-user-plus mr-1"></i> Register
                </button>
            </div>
        </div>
    </div>
</nav>
