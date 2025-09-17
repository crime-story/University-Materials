import numpy as np
from scipy.special import comb
class Generator():
    def kernel_value(self, x, kernel_option):
        m, n = np.shape(x)
        p = np.zeros((m, n))
        for i in range(n):
            k = self.Normalized(x[:, i])
            p[:, i] = k
        kernel_value = self.cal_kernel_value(p, kernel_option)
        return kernel_value
    def cal_kernel_value(self, x, kernel_option):
        m, n = np.shape(x)
        if kernel_option == 1:
            kernel_value = x
        elif kernel_option == 2:
            k1 = int(comb(n, 1))
            k2 = int(comb(n+1, 2))
            k = 0
            kernel_value = np.zeros((m, k1+k2))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i>= j:
                        kernel_value[:, k1+k] = x[:, i]*x[:, j]
                        k = k + 1
                    else:
                        pass
        elif kernel_option == 3:
            k1 = int(comb(n, 1))
            k2 = int(comb(n+1, 2))
            k3 = int(comb(n+2, 3))
            k = 0
            kernel_value = np.zeros((m, k1 + k2 + k3))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i >= j:
                        kernel_value[:, k1+k] = x[:, i]*x[:, j]
                        k = k + 1
                    else:
                        pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        if i >= j:
                            if j >= l:
                                kernel_value[:, k1+k2+k] = x[:, i]*x[:, j]*x[:, l]
                                k = k + 1
                            else:
                                pass
                        else:
                            pass
        elif kernel_option == 4:
            k1 = int(comb(n, 1))
            k2 = int(comb(n+1, 2))
            k3 = int(comb(n+2, 3))
            k4 = int(comb(n+3, 4))
            k = 0
            kernel_value = np.zeros((m, k1 + k2 + k3 + k4))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i >= j:
                        kernel_value[:, k1+k] = x[:, i]*x[:, j]
                        k = k + 1
                    else:
                        pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        if i >= j:
                            if j >= l:
                                kernel_value[:, k1+k2+k] = x[:, i]*x[:, j]*x[:, l]
                                k = k + 1
                            else:
                                pass
                        else:
                            pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            if i >= j:
                                if j >= l:
                                    if l >= h:
                                        kernel_value[:, k1+k2+k3+k] = x[:, i]*x[:, j]*x[:, l]*x[:, h]
                                        k = k + 1
                                    else:
                                        pass
                                else:
                                    pass
                            else:
                                pass
        elif kernel_option == 5:
            k1 = int(comb(n, 1))
            k2 = int(comb(n + 1, 2))
            k3 = int(comb(n + 2, 3))
            k4 = int(comb(n + 3, 4))
            k5 = int(comb(n + 4, 5))
            k = 0
            kernel_value = np.zeros((m, k1 + k2 + k3 + k4 + k5))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i >= j:
                        kernel_value[:, k1 + k] = x[:, i] * x[:, j]
                        k = k + 1
                    else:
                        pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        if i >= j:
                            if j >= l:
                                kernel_value[:, k1 + k2 + k] = x[:, i] * x[:, j] * x[:, l]
                                k = k + 1
                            else:
                                pass
                        else:
                            pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            if i >= j:
                                if j >= l:
                                    if l >= h:
                                        kernel_value[:, k1 + k2 + k3 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]
                                        k = k + 1
                                    else:
                                        pass
                                else:
                                    pass
                            else:
                                pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                if i >= j:
                                    if j >= l:
                                        if l >= h:
                                            if h >= o:
                                                kernel_value[:, k1 + k2 + k3 + k4 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]* x[:, o]
                                                k = k + 1
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
        elif kernel_option == 6:
            k1 = int(comb(n, 1))
            k2 = int(comb(n + 1, 2))
            k3 = int(comb(n + 2, 3))
            k4 = int(comb(n + 3, 4))
            k5 = int(comb(n + 4, 5))
            k6 = int(comb(n + 5, 6))
            k = 0
            kernel_value = np.zeros((m, k1 + k2 + k3 + k4 + k5+ k6))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i >= j:
                        kernel_value[:, k1 + k] = x[:, i] * x[:, j]
                        k = k + 1
                    else:
                        pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        if i >= j:
                            if j >= l:
                                kernel_value[:, k1 + k2 + k] = x[:, i] * x[:, j] * x[:, l]
                                k = k + 1
                            else:
                                pass
                        else:
                            pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            if i >= j:
                                if j >= l:
                                    if l >= h:
                                        kernel_value[:, k1 + k2 + k3 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]
                                        k = k + 1
                                    else:
                                        pass
                                else:
                                    pass
                            else:
                                pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                if i >= j:
                                    if j >= l:
                                        if l >= h:
                                            if h >= o:
                                                kernel_value[:, k1 + k2 + k3 + k4 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]* x[:, o]
                                                k = k + 1
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                for p in range(n):
                                    if i >= j:
                                        if j >= l:
                                            if l >= h:
                                                if h >= o:
                                                    if o >= p:
                                                        kernel_value[:, k1 + k2 + k3 + k4 + k5 +k] = x[:, i] * x[:, j] * x[:, l] * x[:, h] * x[:,o]* x[:,p]
                                                        k = k + 1
                                                    else:
                                                        pass
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
        elif kernel_option == 7:
            k1 = int(comb(n, 1))
            k2 = int(comb(n + 1, 2))
            k3 = int(comb(n + 2, 3))
            k4 = int(comb(n + 3, 4))
            k5 = int(comb(n + 4, 5))
            k6 = int(comb(n + 5, 6))
            k7 = int(comb(n + 6, 7))
            k = 0
            kernel_value = np.zeros((m, k1 + k2 + k3 + k4 + k5 + k6 + k7))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i >= j:
                        kernel_value[:, k1 + k] = x[:, i] * x[:, j]
                        k = k + 1
                    else:
                        pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        if i >= j:
                            if j >= l:
                                kernel_value[:, k1 + k2 + k] = x[:, i] * x[:, j] * x[:, l]
                                k = k + 1
                            else:
                                pass
                        else:
                            pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            if i >= j:
                                if j >= l:
                                    if l >= h:
                                        kernel_value[:, k1 + k2 + k3 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]
                                        k = k + 1
                                    else:
                                        pass
                                else:
                                    pass
                            else:
                                pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                if i >= j:
                                    if j >= l:
                                        if l >= h:
                                            if h >= o:
                                                kernel_value[:, k1 + k2 + k3 + k4 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]* x[:, o]
                                                k = k + 1
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                for p in range(n):
                                    if i >= j:
                                        if j >= l:
                                            if l >= h:
                                                if h >= o:
                                                    if o >= p:
                                                        kernel_value[:, k1 + k2 + k3 + k4 + k5 +k] = x[:, i] * x[:, j] * x[:, l] * x[:, h] * x[:,o]* x[:,p]
                                                        k = k + 1
                                                    else:
                                                        pass
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                for p in range(n):
                                    for q in range(n):
                                        if i >= j:
                                            if j >= l:
                                                if l >= h:
                                                    if h >= o:
                                                        if o >= p:
                                                            if p >= q:
                                                                kernel_value[:, k1 + k2 + k3 + k4 + k5 + k6 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h] * x[:, o] * x[:, p] * x[:, q]
                                                                k = k + 1
                                                            else:
                                                                pass
                                                    else:
                                                        pass
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
        else:
            k1 = int(comb(n, 1))
            k2 = int(comb(n + 1, 2))
            k3 = int(comb(n + 2, 3))
            k4 = int(comb(n + 3, 4))
            k5 = int(comb(n + 4, 5))
            k6 = int(comb(n + 5, 6))
            k7 = int(comb(n + 6, 7))
            k8 = int(comb(n + 7, 8))
            k = 0
            kernel_value = np.zeros((m, k1 + k2 + k3 + k4 + k5 + k6 + k7 + k8))
            for i in range(k1):
                kernel_value[:, i] = x[:, i]
            for i in range(n):
                for j in range(n):
                    if i >= j:
                        kernel_value[:, k1 + k] = x[:, i] * x[:, j]
                        k = k + 1
                    else:
                        pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        if i >= j:
                            if j >= l:
                                kernel_value[:, k1 + k2 + k] = x[:, i] * x[:, j] * x[:, l]
                                k = k + 1
                            else:
                                pass
                        else:
                            pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            if i >= j:
                                if j >= l:
                                    if l >= h:
                                        kernel_value[:, k1 + k2 + k3 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]
                                        k = k + 1
                                    else:
                                        pass
                                else:
                                    pass
                            else:
                                pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                if i >= j:
                                    if j >= l:
                                        if l >= h:
                                            if h >= o:
                                                kernel_value[:, k1 + k2 + k3 + k4 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h]* x[:, o]
                                                k = k + 1
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                for p in range(n):
                                    if i >= j:
                                        if j >= l:
                                            if l >= h:
                                                if h >= o:
                                                    if o >= p:
                                                        kernel_value[:, k1 + k2 + k3 + k4 + k5 +k] = x[:, i] * x[:, j] * x[:, l] * x[:, h] * x[:,o]* x[:,p]
                                                        k = k + 1
                                                    else:
                                                        pass
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                for p in range(n):
                                    for q in range(n):
                                        if i >= j:
                                            if j >= l:
                                                if l >= h:
                                                    if h >= o:
                                                        if o >= p:
                                                            if p >= q:
                                                                kernel_value[:, k1 + k2 + k3 + k4 + k5 + k6 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h] * x[:, o] * x[:, p] * x[:, q]
                                                                k = k + 1
                                                            else:
                                                                pass
                                                    else:
                                                        pass
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
            k = 0
            for i in range(n):
                for j in range(n):
                    for l in range(n):
                        for h in range(n):
                            for o in range(n):
                                for p in range(n):
                                    for q in range(n):
                                        for r in range(n):
                                            if i >= j:
                                                if j >= l:
                                                    if l >= h:
                                                        if h >= o:
                                                            if o >= p:
                                                                if p >= q:
                                                                    if q >= r:
                                                                        kernel_value[:, k1 + k2 + k3 + k4 + k5 + k6 + k7 + k] = x[:, i] * x[:, j] * x[:, l] * x[:, h] * x[:, o] * x[:, p] * x[:, q] * x[:, r]
                                                                        k = k + 1
                                                                    else:
                                                                        pass
                                                                else:
                                                                    pass
                                                            else:
                                                                pass
                                                    else:
                                                        pass
                                            else:
                                                pass
                                        else:
                                            pass
                                    else:
                                        pass
                                else:
                                    pass
        return kernel_value
    def Normalized(self, x):
        k_1 = max(x)
        k_2 = min(x)
        m = len(x)
        k = np.zeros(m)
        for i in range(m):
            k[i] = ((x[i]-k_2)/(k_1-k_2))
        return k
    def neuron(self, kernel_value):
        spikes = kernel_value
        return spikes
