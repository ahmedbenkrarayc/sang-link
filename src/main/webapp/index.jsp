<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="view/fragments/head.jsp"/>
    <title>BloodBridge - Save Lives, Donate Blood</title>
</head>
<body class="bg-gray-50">
<jsp:include page="view/fragments/nav.jsp"/>

<section class="bg-gradient-to-r from-red-600 to-red-800 text-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
        <div class="text-center">
            <h1 class="text-4xl md:text-6xl font-bold mb-6">Save Lives, Donate Blood</h1>
            <p class="text-xl md:text-2xl mb-8 max-w-3xl mx-auto">
                Join thousands of heroes who are making a difference in their community.
                Your single donation can save up to three lives.
            </p>
            <div class="flex flex-col sm:flex-row gap-4 justify-center">
                <a href="/donors/register"
                   class="bg-white text-red-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition duration-300">
                    Become a Donor
                </a>
                <a href="/receivers/register"
                   class="border-2 border-white text-white px-8 py-4 rounded-lg font-semibold text-lg hover:bg-white hover:text-red-600 transition duration-300">
                    Find Blood
                </a>
            </div>
        </div>
    </div>
</section>

<section class="bg-white py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8 text-center">
            <div class="p-6">
                <div class="text-3xl font-bold text-red-600 mb-2" id="donorsCount">0</div>
                <div class="text-gray-600">Active Donors</div>
            </div>
            <div class="p-6">
                <div class="text-3xl font-bold text-red-600 mb-2" id="livesSaved">0</div>
                <div class="text-gray-600">Lives Saved</div>
            </div>
            <div class="p-6">
                <div class="text-3xl font-bold text-red-600 mb-2" id="donationsCount">0</div>
                <div class="text-gray-600">Total Donations</div>
            </div>
            <div class="p-6">
                <div class="text-3xl font-bold text-red-600 mb-2" id="emergenciesCount">0</div>
                <div class="text-gray-600">Emergencies Helped</div>
            </div>
        </div>
    </div>
</section>

<section class="bg-gray-100 py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
            <h2 class="text-3xl font-bold text-gray-900 mb-4">How It Works</h2>
            <p class="text-lg text-gray-600 max-w-2xl mx-auto">
                Simple steps to become a life-saver or get the help you need
            </p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="bg-white p-8 rounded-lg shadow-md text-center">
                <div class="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                    1
                </div>
                <h3 class="text-xl font-semibold text-gray-900 mb-4">Register</h3>
                <p class="text-gray-600">
                    Sign up as a donor or receiver with your basic information and blood type details.
                </p>
            </div>

            <div class="bg-white p-8 rounded-lg shadow-md text-center">
                <div class="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                    2
                </div>
                <h3 class="text-xl font-semibold text-gray-900 mb-4">Connect</h3>
                <p class="text-gray-600">
                    Our system matches donors with receivers based on blood type compatibility and location.
                </p>
            </div>

            <div class="bg-white p-8 rounded-lg shadow-md text-center">
                <div class="w-16 h-16 bg-red-100 text-red-600 rounded-full flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                    3
                </div>
                <h3 class="text-xl font-semibold text-gray-900 mb-4">Save Lives</h3>
                <p class="text-gray-600">
                    Donate blood and directly impact lives, or receive the blood you urgently need.
                </p>
            </div>
        </div>
    </div>
</section>

<section class="bg-white py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
            <h2 class="text-3xl font-bold text-gray-900 mb-4">Blood Types Compatibility</h2>
            <p class="text-lg text-gray-600">
                Understanding blood types is crucial for safe transfusions
            </p>
        </div>

        <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
            <c:forEach var="bloodGroup" items="${bloodGroups}">
                <div class="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
                    <div class="text-2xl font-bold text-red-600 mb-2">
                            ${bloodGroup.name().split("_")[1].startsWith("P") ? bloodGroup.name().split("_")[0].concat("+") : bloodGroup.name().split("_")[0].concat("-")}
                    </div>
                    <div class="text-sm text-gray-600">
                        <c:choose>
                            <c:when test="${bloodGroup.name().contains('O_NEGATIVE')}">Universal Donor</c:when>
                            <c:when test="${bloodGroup.name().contains('AB_POSITIVE')}">Universal Receiver</c:when>
                            <c:otherwise>Specific Compatibility</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<section class="bg-red-600 text-white py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl font-bold mb-4">Emergency Blood Need?</h2>
        <p class="text-xl mb-8 max-w-2xl mx-auto">
            If you need blood urgently, register as a receiver and we'll help you find compatible donors immediately.
        </p>
        <a href="/receivers/register"
           class="bg-white text-red-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-gray-100 transition duration-300 inline-block">
            Get Emergency Help
        </a>
    </div>
</section>

<section class="bg-gray-100 py-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-12">
            <h2 class="text-3xl font-bold text-gray-900 mb-4">Stories That Inspire</h2>
            <p class="text-lg text-gray-600">Real stories from our donors and receivers</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="bg-white p-6 rounded-lg shadow-md">
                <div class="text-yellow-400 mb-4">
                    ★★★★★
                </div>
                <p class="text-gray-600 mb-4">
                    "Thanks to BloodBridge, I found a matching donor within hours when my father needed emergency surgery. You're truly life-savers!"
                </p>
                <div class="font-semibold text-gray-900">- Sarah M.</div>
            </div>

            <div class="bg-white p-6 rounded-lg shadow-md">
                <div class="text-yellow-400 mb-4">
                    ★★★★★
                </div>
                <p class="text-gray-600 mb-4">
                    "Donating blood through this platform made me realize how easy it is to make a difference. The process was smooth and professional."
                </p>
                <div class="font-semibold text-gray-900">- James K.</div>
            </div>

            <div class="bg-white p-6 rounded-lg shadow-md">
                <div class="text-yellow-400 mb-4">
                    ★★★★★
                </div>
                <p class="text-gray-600 mb-4">
                    "As a regular donor, I love how the system connects me directly with people in need. It's rewarding to see the direct impact."
                </p>
                <div class="font-semibold text-gray-900">- Maria L.</div>
            </div>
        </div>
    </div>
</section>

<section class="bg-white py-16">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl font-bold text-gray-900 mb-4">Ready to Make a Difference?</h2>
        <p class="text-lg text-gray-600 mb-8">
            Join our community today and be part of something bigger. Whether you're donating or receiving,
            you're contributing to a life-saving mission.
        </p>
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <a href="/donors/register"
               class="bg-red-600 text-white px-8 py-4 rounded-lg font-semibold text-lg hover:bg-red-700 transition duration-300">
                Start Donating
            </a>
            <a href="/about"
               class="border-2 border-red-600 text-red-600 px-8 py-4 rounded-lg font-semibold text-lg hover:bg-red-600 hover:text-white transition duration-300">
                Learn More
            </a>
        </div>
    </div>
</section>

<jsp:include page="view/fragments/footer.jsp"/>

<script>
    function animateCounter(element, target, duration) {
        let start = 0;
        const increment = target / (duration / 16);
        const timer = setInterval(() => {
            start += increment;
            if (start >= target) {
                element.textContent = Math.floor(target).toLocaleString();
                clearInterval(timer);
            } else {
                element.textContent = Math.floor(start).toLocaleString();
            }
        }, 16);
    }

    document.addEventListener('DOMContentLoaded', function() {
        setTimeout(() => {
            animateCounter(document.getElementById('donorsCount'), 1250, 2000);
            animateCounter(document.getElementById('livesSaved'), 3750, 2000);
            animateCounter(document.getElementById('donationsCount'), 2800, 2000);
            animateCounter(document.getElementById('emergenciesCount'), 650, 2000);
        }, 500);
    });
</script>
</body>
</html>