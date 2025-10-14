<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
</head>
<body class="bg-gray-50 min-h-screen">
<jsp:include page="../fragments/nav.jsp"/>

<div class="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
    <div class="text-center mb-10">
        <h1 class="text-3xl font-bold text-gray-900">Donor Registration</h1>
        <p class="mt-3 text-lg text-gray-600">Join our community of life-saving donors</p>
    </div>

    <c:if test="${not empty success}">
        <div id="successAlert" class="bg-green-50 border-l-4 border-green-500 p-4 mb-6 rounded-md shadow-sm">
            <div class="flex">
                <div class="flex-shrink-0">
                    <i class="fas fa-check-circle text-green-500"></i>
                </div>
                <div class="ml-3">
                    <p class="text-sm text-green-700">
                        <span class="font-medium">Success!</span> ${success}
                    </p>
                </div>
                <div class="ml-auto pl-3">
                    <div class="-mx-1.5 -my-1.5">
                        <button type="button" onclick="document.getElementById('successAlert').classList.add('hidden')" class="inline-flex bg-green-50 rounded-md p-1.5 text-green-500 hover:bg-green-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-green-50 focus:ring-green-600">
                            <span class="sr-only">Dismiss</span>
                            <i class="fas fa-times h-4 w-4"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${not empty errors}">
        <div id="errorAlert" class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded-md shadow-sm">
            <div class="flex">
                <div class="flex-shrink-0">
                    <i class="fas fa-exclamation-circle text-red-500"></i>
                </div>
                <div class="ml-3">
                    <p class="text-sm text-red-700">
                        <span class="font-medium">Error!</span> Please check the form for errors and try again.
                    </p>
                    <ul class="mt-2 text-xs text-red-600 list-disc list-inside">
                        <c:forEach var="error" items="${errors}">
                            <li>${error}</li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="ml-auto pl-3">
                    <div class="-mx-1.5 -my-1.5">
                        <button type="button" onclick="document.getElementById('errorAlert').classList.add('hidden')" class="inline-flex bg-red-50 rounded-md p-1.5 text-red-500 hover:bg-red-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-red-50 focus:ring-red-600">
                            <span class="sr-only">Dismiss</span>
                            <i class="fas fa-times h-4 w-4"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <div class="bg-white shadow rounded-lg overflow-hidden">
        <div class="px-6 py-5 border-b border-gray-200">
            <h3 class="text-lg leading-6 font-medium text-gray-900">Personal Information</h3>
            <p class="mt-1 text-sm text-gray-500">Please provide your basic information.</p>
        </div>
        <form id="donorForm" method="POST" class="px-6 py-6">
            <div class="grid grid-cols-1 gap-6 sm:grid-cols-2">
                <div>
                    <label for="cin" class="block text-sm font-medium text-gray-700">CIN (National ID)</label>
                    <input type="text" name="cin" id="cin"
                           value="<c:if test="${empty success}">${param.cin}</c:if>"
                           required
                           class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                </div>

                <div>
                    <label for="nom" class="block text-sm font-medium text-gray-700">Last Name</label>
                    <input type="text" name="nom" id="nom"
                           value="<c:if test="${empty success}">${param.nom}</c:if>"
                           required
                           class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                </div>

                <div>
                    <label for="prenom" class="block text-sm font-medium text-gray-700">First Name</label>
                    <input type="text" name="prenom" id="prenom"
                           value="<c:if test="${empty success}">${param.prenom}</c:if>"
                           required
                           class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                </div>

                <div>
                    <label for="phone" class="block text-sm font-medium text-gray-700">Phone Number</label>
                    <input type="tel" name="phone" id="phone"
                           value="<c:if test="${empty success}">${param.phone}</c:if>"
                           required
                           class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                </div>

                <div>
                    <label for="birthday" class="block text-sm font-medium text-gray-700">Date of Birth</label>
                    <input type="date" name="birthday" id="birthday"
                           value="<c:if test="${empty success}">${param.birthday}</c:if>"
                           required
                           class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                </div>

                <div>
                    <label for="gender" class="block text-sm font-medium text-gray-700">Gender</label>
                    <select id="gender" name="gender" required
                            class="mt-1 block w-full bg-white border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                        <option value="" selected disabled>Select gender</option>
                        <c:forEach var="gender" items="${genders}">
                            <option value="${gender}"
                                    <c:if test="${empty success and param.gender == gender}">selected</c:if>>
                                    ${gender}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label for="bloodGroup" class="block text-sm font-medium text-gray-700">Blood Group</label>
                    <select id="bloodGroup" name="bloodGroup" required
                            class="mt-1 block w-full bg-white border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                        <option value="" selected disabled>Select blood group</option>
                        <c:forEach var="bloodGroup" items="${bloodGroups}">
                            <option value="${bloodGroup}"
                                    <c:if test="${empty success and param.bloodGroup == bloodGroup}">selected</c:if>>
                                    ${bloodGroup.name().split("_")[1].startsWith("P") ? bloodGroup.name().split("_")[0].concat("+") : bloodGroup.name().split("_")[0].concat("-")}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Weight -->
                <div>
                    <label for="weight" class="block text-sm font-medium text-gray-700">Weight (kg)</label>
                    <input type="number" step="0.1" name="weight" id="weight"
                           value="<c:if test="${empty success}">${param.weight}</c:if>"
                           required
                           class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-primary focus:border-primary sm:text-sm">
                </div>

                <!-- Medical Conditions -->
                <div class="sm:col-span-2">
                    <h4 class="text-md font-medium text-gray-900 mb-4">Medical Assessment</h4>
                    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                        <!-- Hepatitis B -->
                        <div class="flex items-center">
                            <input id="hepatiteB" name="hepatiteB" type="checkbox" value="true"
                                   <c:if test="${empty success and param.hepatiteB}">checked</c:if>
                                   class="focus:ring-primary h-4 w-4 text-primary border-gray-300 rounded">
                            <label for="hepatiteB" class="ml-2 block text-sm text-gray-700">Hepatitis B</label>
                        </div>

                        <!-- Hepatitis C -->
                        <div class="flex items-center">
                            <input id="hepatiteC" name="hepatiteC" type="checkbox" value="true"
                                   <c:if test="${empty success and param.hepatiteC}">checked</c:if>
                                   class="focus:ring-primary h-4 w-4 text-primary border-gray-300 rounded">
                            <label for="hepatiteC" class="ml-2 block text-sm text-gray-700">Hepatitis C</label>
                        </div>

                        <!-- HIV -->
                        <div class="flex items-center">
                            <input id="vih" name="vih" type="checkbox" value="true"
                                   <c:if test="${empty success and param.vih}">checked</c:if>
                                   class="focus:ring-primary h-4 w-4 text-primary border-gray-300 rounded">
                            <label for="vih" class="ml-2 block text-sm text-gray-700">HIV</label>
                        </div>

                        <!-- Diabete Insulin -->
                        <div class="flex items-center">
                            <input id="diabeteInsulin" name="diabeteInsulin" type="checkbox" value="true"
                                   <c:if test="${empty success and param.diabeteInsulin}">checked</c:if>
                                   class="focus:ring-primary h-4 w-4 text-primary border-gray-300 rounded">
                            <label for="diabeteInsulin" class="ml-2 block text-sm text-gray-700">Diabetes (Insulin)</label>
                        </div>

                        <!-- Grossesse -->
                        <div class="flex items-center">
                            <input id="grossesse" name="grossesse" type="checkbox" value="true"
                                   <c:if test="${empty success and param.grossesse}">checked</c:if>
                                   class="focus:ring-primary h-4 w-4 text-primary border-gray-300 rounded">
                            <label for="grossesse" class="ml-2 block text-sm text-gray-700">Pregnancy</label>
                        </div>

                        <!-- Allaitement -->
                        <div class="flex items-center">
                            <input id="allaitement" name="allaitement" type="checkbox" value="true"
                                   <c:if test="${empty success and param.allaitement}">checked</c:if>
                                   class="focus:ring-primary h-4 w-4 text-primary border-gray-300 rounded">
                            <label for="allaitement" class="ml-2 block text-sm text-gray-700">Breastfeeding</label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="mt-8 flex justify-end space-x-3">
                <button type="button" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
                    Cancel
                </button>
                <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary hover:bg-secondary focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary">
                    <i class="fas fa-save mr-2"></i> Save Donor
                </button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
</body>
</html>